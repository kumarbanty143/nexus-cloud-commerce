package consumer;

import dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import service.EmailService;
import service.SmsService;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {
    private final EmailService emailService;
    private final SmsService smsService;

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void consume(NotificationEvent event){
        if("EMAIL".equals(event.getType())) {
            emailService.sendEmail(event.getEmail(), event.getSubject(), event.getMessage());
        }
        if("SMS".equals(event.getType())) {
            smsService.sendSms(event.getMobile(), event.getMessage());
        }
    }
}
