package controller;

import dto.PaymentRequestDto;
import dto.PaymentResponseDto;
import dto.PaymentStatusUpdateRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.PaymentService;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody @Valid PaymentRequestDto request){
        PaymentResponseDto response = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDto> getByOrderId(@PathVariable("orderId") String  orderId){
        PaymentResponseDto response = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{paymentId}/status")
    public ResponseEntity<PaymentResponseDto> underpaymentStatus(@PathVariable("paymentId") Long paymentId, @RequestBody @Valid PaymentStatusUpdateRequestDto request){
        PaymentResponseDto response = paymentService.updatePaymentStatus(paymentId, request);
        return ResponseEntity.ok(response);
    }

}
