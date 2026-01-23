package impl;

import dto.PaymentRequestDto;
import dto.PaymentResponseDto;
import dto.PaymentStatusUpdateRequestDto;
import entity.Payment;
import enums.PaymentStatus;
import exception.PaymentNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.PaymentRepository;
import service.PaymentService;
import util.TransactionGenerator;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    @Override
    public PaymentResponseDto createPayment(PaymentRequestDto request) {
        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .amount(request.getAmount())
                .paymentStatus(PaymentStatus.PENDING)
                .paymentMethod(request.getPaymentMethod())
                .transactionId(TransactionGenerator.generate())
                .build();

        Payment saved = paymentRepository.save(payment);
        return mapToPaymentResponse(saved);
    }

    @Override
    public PaymentResponseDto getPaymentByOrderId(String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(()-> new PaymentNotFoundException("Payment not found for this orderId: "+ orderId));
        return mapToPaymentResponse(payment);
    }

    @Override
    public PaymentResponseDto updatePaymentStatus(Long paymentId, PaymentStatusUpdateRequestDto request) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(()->new PaymentNotFoundException("Payment not found with id: "+paymentId));
        payment.setPaymentStatus(request.getPaymentStatus());
        return mapToPaymentResponse(payment);
    }

    private PaymentResponseDto mapToPaymentResponse(Payment payment){
        return new PaymentResponseDto(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getPaymentStatus(),
                payment.getTransactionId()
        );
    }
}
