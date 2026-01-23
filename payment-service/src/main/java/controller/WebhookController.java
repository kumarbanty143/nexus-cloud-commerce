package controller;

import impl.RazorpayWebhookImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class WebhookController {
    private RazorpayWebhookImpl webhookService;

    @PostMapping("/webhook/razorpay")
    public ResponseEntity<Void> handleWebhook(@RequestHeader("X-Razorpay-Signature") String signature, @RequestBody String payload){
        webhookService.processWebhook(signature, payload);
        return ResponseEntity.ok().build();
    }
}
