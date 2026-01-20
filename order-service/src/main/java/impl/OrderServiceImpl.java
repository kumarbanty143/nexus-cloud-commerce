package impl;

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

    @Override
    public OrderResponseDto placeOrder(OrderRequestDto orderRequestDto) {
        Order order = orderMapper.toEntity(orderRequestDto);
        order.setOrderNo(UUID.randomUUID().toString());
        order.setOrderStatus("CREATED");
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }
}
