package com.example.order.orchestrator;

import com.example.order.model.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventHandler {

    private final OrderOrchestrator orchestrator;

    public EventHandler(OrderOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @KafkaListener(topics = "order-events", groupId = "order-orchestrator-group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(OrderEvent event) {
        System.out.println("Received event: " + event.getStatus() + " for order " + event.getOrderId());
        orchestrator.handle(event);
    }
}
