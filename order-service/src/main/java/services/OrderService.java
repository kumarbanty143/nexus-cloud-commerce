package services;

import dto.OrderRequestDto;
import dto.OrderResponseDto;

public interface OrderService {
    OrderResponseDto placeOrder(OrderRequestDto orderRequestDto);
}
