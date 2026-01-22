package consumer;

import event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import services.InventoryService;

@Service
@RequiredArgsConstructor
public class OrderEventConsumer {
    private final InventoryService inventoryService;
    @KafkaListener(topics = "order-event", groupId = "inventory-group")
    public void consume(OrderPlacedEvent event){
        inventoryService.updateStock(event);
    }
}
