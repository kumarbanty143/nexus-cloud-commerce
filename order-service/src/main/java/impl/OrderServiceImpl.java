package impl;

import client.InventoryFeignClient;
import dto.InventoryResponseDto;
import dto.OrderRequestDto;
import dto.OrderResponseDto;
import entity.Order;
import event.OrderPlacedEvent;
import kafka.OrderEventProducer;
import lombok.RequiredArgsConstructor;
import mapper.OrderMapper;
import org.springframework.stereotype.Service;
import repository.OrderRepository;
import services.OrderService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final InventoryFeignClient inventoryFeignClient;
    private OrderEventProducer orderEventProducer;

    @Override
    public OrderResponseDto placeOrder(OrderRequestDto orderRequestDto) {
        InventoryResponseDto inventoryResponseDto = inventoryFeignClient.inInStock(orderRequestDto.getSkuCode());
        if(!inventoryResponseDto.isInStock()){
            throw  new RuntimeException("Order is out of stock");
        }
        Order order = orderMapper.toEntity(orderRequestDto);
        String orderId = UUID.randomUUID().toString();
        order.setOrderNo(orderId);
        order.setOrderStatus("CREATED");
        orderRepository.save(order);

        OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
        orderPlacedEvent.setEventId(UUID.randomUUID().toString());
        orderPlacedEvent.setOrderId(orderId);
        orderPlacedEvent.setSkuCode(orderRequestDto.getSkuCode());
        orderPlacedEvent.setQuantity(orderPlacedEvent.getQuantity());
        orderPlacedEvent.setEventTime(LocalDateTime.now());
        orderEventProducer.sendOrderEvent(orderPlacedEvent);

        return orderMapper.toDto(order);
    }
}
