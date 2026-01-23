package consumer;

import dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import service.EmailService;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {
    private final EmailService emailService;

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void consume(NotificationEvent event){
        emailService.sendEmail(event.getEmail(), event.getSubject(), event.getMessage());
    }
}
