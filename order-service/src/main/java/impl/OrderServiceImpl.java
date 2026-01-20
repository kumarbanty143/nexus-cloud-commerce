package impl;

import client.InventoryFeignClient;
import dto.InventoryResponseDto;
import dto.OrderRequestDto;
import dto.OrderResponseDto;
import entity.Order;
import lombok.RequiredArgsConstructor;
import mapper.OrderMapper;
import org.springframework.stereotype.Service;
import repository.OrderRepository;
import services.OrderService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final InventoryFeignClient inventoryFeignClient;

    @Override
    public OrderResponseDto placeOrder(OrderRequestDto orderRequestDto) {
        InventoryResponseDto inventoryResponseDto = inventoryFeignClient.inInStock(orderRequestDto.getSkuCode());
        if(!inventoryResponseDto.isInStock()){
            throw  new RuntimeException("Order is out of stock");
        }
        Order order = orderMapper.toEntity(orderRequestDto);
        order.setOrderNo(UUID.randomUUID().toString());
        order.setOrderStatus("CREATED");
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }
}
