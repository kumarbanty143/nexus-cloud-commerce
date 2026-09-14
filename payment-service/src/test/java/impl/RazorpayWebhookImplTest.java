package impl;

import client.InventoryClient;
import client.OrderClient;
import entity.Payment;
import enums.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import repository.PaymentRepository;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RazorpayWebhookImplTest {
    private final PaymentRepository payments = mock(PaymentRepository.class);
    private final OrderClient orders = mock(OrderClient.class);
    private final InventoryClient inventory = mock(InventoryClient.class);
    private final RazorpayWebhookImpl service = new RazorpayWebhookImpl(payments, orders, inventory);
    private static final String SECRET = "local-test-secret";

    private String payload(String event) {
        return "{\"event\":\"" + event + "\",\"payload\":{\"payment\":{\"entity\":{\"order_id\":\"rzp-order-1\",\"id\":\"pay-1\"}}}}";
    }
    private void send(String event) throws Exception {
        ReflectionTestUtils.setField(service, "webhookSecret", SECRET);
        var mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        var payload = payload(event);
        service.processWebhook(HexFormat.of().formatHex(mac.doFinal(payload.getBytes(StandardCharsets.UTF_8))), payload);
    }
    private Payment payment(PaymentStatus status) {
        var payment = Payment.builder().orderId("order-1").transactionId("rzp-order-1").paymentStatus(status).build();
        when(payments.findByTransactionId("rzp-order-1")).thenReturn(Optional.of(payment));
        return payment;
    }
    @Test void capturedEventPersistsSuccessAndGatewayId() throws Exception {
        var payment = payment(PaymentStatus.PENDING);
        send("payment.captured");
        assertEquals(PaymentStatus.SUCCESS, payment.getPaymentStatus());
        assertEquals("pay-1", payment.getGatewayPaymentId());
        verify(orders).confirmOrder("order-1");
        verify(inventory).commitInventory("order-1");
        verify(payments).save(payment);
    }
    @Test void failedEventPersistsFailure() throws Exception {
        var payment = payment(PaymentStatus.PENDING);
        send("payment.failed");
        assertEquals(PaymentStatus.FAILED, payment.getPaymentStatus());
        verify(inventory).rollbackInventory("order-1");
        verify(orders).failOrder("order-1");
        verify(payments).save(payment);
    }
    @Test void capturedDuplicateDoesNotRepeatSideEffects() throws Exception {
        payment(PaymentStatus.SUCCESS);
        send("payment.captured");
        verifyNoInteractions(orders, inventory);
        verify(payments, never()).save(any());
    }
    @Test void staleFailureCannotOverwriteSuccess() throws Exception {
        var payment = payment(PaymentStatus.SUCCESS);
        send("payment.failed");
        assertEquals(PaymentStatus.SUCCESS, payment.getPaymentStatus());
        verifyNoInteractions(orders, inventory);
        verify(payments, never()).save(any());
    }
    @Test void invalidSignatureCannotTouchPaymentState() {
        ReflectionTestUtils.setField(service, "webhookSecret", SECRET);
        assertThrows(SecurityException.class, () -> service.processWebhook("invalid", payload("payment.captured")));
        verifyNoInteractions(payments, orders, inventory);
    }
}
