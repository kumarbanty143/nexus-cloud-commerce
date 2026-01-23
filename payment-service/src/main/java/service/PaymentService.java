package service;

import dto.PaymentRequestDto;
import dto.PaymentResponseDto;
import dto.PaymentStatusUpdateRequestDto;

public interface PaymentService {
    PaymentResponseDto createPayment(PaymentRequestDto request);
    PaymentResponseDto getPaymentByOrderId(String orderId);
    PaymentResponseDto updatePaymentStatus(Long paymentId, PaymentStatusUpdateRequestDto request);
}
