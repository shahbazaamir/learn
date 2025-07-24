package com.example.inventory;
import com.example.order.model.OrderEvent;
import com.example.order.model.OrderStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaHandler {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaHandler(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    @KafkaListener(topics = "inventory-topic", groupId = "order-orchestrator-group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(OrderEvent event) {
        System.out.println("Received event: " + event.getStatus() + " for order " + event.getOrderId());
        if ( OrderStatus.INVENTORY_VALIDATE == event.getStatus() ) {
            boolean inventoryValidation = true;
            if(inventoryValidation) {
                kafkaTemplate.send("order-events", new OrderEvent(event.getOrderId(), OrderStatus.INVENTORY_RESERVED));
            } else {
                kafkaTemplate.send("order-events", new OrderEvent(event.getOrderId(), OrderStatus.INVENTORY_RESERVATION_FAILED));
            }
        }
    }
}
