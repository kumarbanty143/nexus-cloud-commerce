package impl;

import client.InventoryFeignClient;
import dto.InventoryResponseDto;
import dto.OrderRequestDto;
import event.OrderPlacedEvent;
import kafka.OrderEventProducer;
import mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import repository.OrderRepository;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {
    private final OrderRepository orders = mock(OrderRepository.class);
    private final InventoryFeignClient inventory = mock(InventoryFeignClient.class);
    private final OrderEventProducer producer = mock(OrderEventProducer.class);
    private final OrderServiceImpl service = new OrderServiceImpl(orders, new OrderMapper(), inventory, producer);

    @Test void publishesRequestedQuantityAndOrderIdentity() {
        when(inventory.inInStock("sku-1")).thenReturn(new InventoryResponseDto("sku-1", true, 8));
        service.placeOrder(new OrderRequestDto("sku-1", 3, BigDecimal.TEN));
        var event = ArgumentCaptor.forClass(OrderPlacedEvent.class);
        var order = ArgumentCaptor.forClass(entity.Order.class);
        verify(orders).save(order.capture());
        verify(producer).sendOrderEvent(event.capture());
        assertEquals(3, event.getValue().getQuantity());
        assertEquals(order.getValue().getOrderNo(), event.getValue().getOrderId());
        assertEquals("sku-1", event.getValue().getSkuCode());
        assertNotNull(event.getValue().getEventId());
    }

    @Test void rejectsInsufficientQuantityBeforeSaving() {
        when(inventory.inInStock("sku-1")).thenReturn(new InventoryResponseDto("sku-1", true, 2));
        assertThrows(RuntimeException.class, () -> service.placeOrder(new OrderRequestDto("sku-1", 3, BigDecimal.TEN)));
        verifyNoInteractions(orders, producer);
    }

    @Test void rejectsMissingOrNonPositiveQuantity() {
        for (Integer quantity : new Integer[]{null, 0, -1}) {
            assertThrows(IllegalArgumentException.class, () -> service.placeOrder(new OrderRequestDto("sku-1", quantity, BigDecimal.TEN)));
        }
        verifyNoInteractions(orders, inventory, producer);
    }
}
