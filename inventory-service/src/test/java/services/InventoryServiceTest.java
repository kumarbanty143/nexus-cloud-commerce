package services;

import entity.Inventory;
import entity.ProcessedOrder;
import event.OrderPlacedEvent;
import mapper.InventoryMapper;
import org.junit.jupiter.api.Test;
import repository.InventoryRepository;
import repository.ProcessedOrderRepository;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceTest {
    private final InventoryRepository inventory = mock(InventoryRepository.class);
    private final ProcessedOrderRepository processed = mock(ProcessedOrderRepository.class);
    private final InventoryService service = new InventoryService(inventory, new InventoryMapper(), processed);

    private OrderPlacedEvent event(Integer quantity) {
        var event = new OrderPlacedEvent();
        event.setOrderId("order-1"); event.setSkuCode("sku-1"); event.setQuantity(quantity);
        return event;
    }

    @Test void newEventDecrementsStockAndRecordsProcessing() {
        var stock = new Inventory(1L, "sku-1", 10);
        when(inventory.findBySkuCode("sku-1")).thenReturn(Optional.of(stock));
        service.updateStock(event(3));
        assertEquals(7, stock.getQuantity());
        verify(inventory).save(stock);
        verify(processed).save(argThat((ProcessedOrder order) -> "order-1".equals(order.getOrderId())));
    }

    @Test void alreadyProcessedOrderIsIgnored() {
        when(processed.existsByOrderId("order-1")).thenReturn(true);
        service.updateStock(event(3));
        verifyNoInteractions(inventory);
        verify(processed, never()).save(any());
    }

    @Test void shortageDoesNotMutateStockOrRecordProcessing() {
        var stock = new Inventory(1L, "sku-1", 2);
        when(inventory.findBySkuCode("sku-1")).thenReturn(Optional.of(stock));
        assertThrows(RuntimeException.class, () -> service.updateStock(event(3)));
        assertEquals(2, stock.getQuantity());
        verify(inventory, never()).save(any());
        verify(processed, never()).save(any());
    }

    @Test void invalidQuantitiesAreRejectedBeforeDatabaseAccess() {
        for (Integer quantity : new Integer[]{null, 0, -1}) {
            assertThrows(IllegalArgumentException.class, () -> service.updateStock(event(quantity)));
        }
        verifyNoInteractions(inventory, processed);
    }
}
