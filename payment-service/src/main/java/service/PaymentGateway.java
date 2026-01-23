package service;

import dto.GatewayOrderResponse;

import java.math.BigDecimal;

public interface PaymentGateway {
    GatewayOrderResponse createOrder(String orderId, BigDecimal amount);
}
