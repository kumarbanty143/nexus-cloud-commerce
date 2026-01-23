package impl;

import client.InventoryClient;
import client.OrderClient;
import com.razorpay.Utils;
import entity.Payment;
import enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import repository.PaymentRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class RazorpayWebhookImpl {
    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final InventoryClient inventoryClient;
    @Value("${razorpay.webhook-secret}")
    private String webhookSecret;

    public void processWebhook(String signature, String payload){
        verifySignature(signature, payload);
        JSONObject event = new JSONObject(payload);
        String eventType = event.getString("event");
        log.info("Received Razorpay webhook event: "+eventType);

        switch (eventType){
            case "payment.captyred" -> handlePaymentSuccess(event);
            case "payment.failed" -> handlePaymentFailed(event);
            default -> log.warn("Unhandled Razorpay event: "+eventType);
        }
    }

    private void verifySignature(String signature, String payload){
        try{
            Utils.verifyWebhookSignature(payload, signature, webhookSecret);
        }catch (Exception e){
            log.error("Invalid Razorpay webhook signature");
            throw  new SecurityException("Invalid Razorpay webhook signature");
        }
    }

    private void handlePaymentSuccess(JSONObject event){
        JSONObject paymentEntity = extractPaymentEntity(event);
        String razorpayOrderId = paymentEntity.getString("order_id");
        String razorPayPaymentId = paymentEntity.getString("id");
        Payment payment = paymentRepository.findByTransactionId(razorpayOrderId)
                .orElseThrow(()-> new IllegalStateException("Payment not found for this Razorpay order id"));
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setGatewayPaymentId(razorPayPaymentId);
        orderClient.confirmOrder(payment.getOrderId());
        inventoryClient.commitInventory(payment.getOrderId());
    }

    private JSONObject extractPaymentEntity(JSONObject event){
        return event.getJSONObject("payload")
                .getJSONObject("payment")
                .getJSONObject("entity");
    }

    private void handlePaymentFailed(JSONObject event){
        JSONObject paymentEntity = extractPaymentEntity(event);
        String razorpayOrderId = paymentEntity.getString("order_id");
        Payment payment = paymentRepository.findByTransactionId(razorpayOrderId)
                .orElseThrow(()-> new IllegalStateException("Payment not found for this Razorpay order id"));
        payment.setPaymentStatus(PaymentStatus.FAILED);
        inventoryClient.rollbackInventory(payment.getOrderId());
        orderClient.failOrder(payment.getOrderId());
    }

}
