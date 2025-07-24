package com.example.order.orchestrator;

import com.example.order.model.OrderEvent;
import com.example.order.model.OrderStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderOrchestrator {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderOrchestrator(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void handle(OrderEvent event) {
        switch (event.getStatus()) {
            case ORDER_PLACED:
                System.out.println("Order placed. Validate inventory.");
                kafkaTemplate.send("inventory-topic", new OrderEvent(event.getOrderId(), OrderStatus.INVENTORY_VALIDATE));
                break;
            case ORDER_CREATED:
                System.out.println("Order created. Reserving inventory.");
                kafkaTemplate.send("inventory-topic", new OrderEvent(event.getOrderId(), OrderStatus.INVENTORY_RESERVED));
                break;
            case INVENTORY_RESERVED:
                System.out.println("Inventory reserved. Processing payment.");
                kafkaTemplate.send("payment-topic", new OrderEvent(event.getOrderId(), OrderStatus.PAYMENT_REQUESTED));
                break;
            case INVENTORY_RESERVATION_FAILED:
                System.out.println("Inventory reserved. Processing payment.");
                System.out.println("Order cancelled.");
                break;
            case PAYMENT_PROCESSED:
                System.out.println("Payment processed. Confirming shipment.");
                kafkaTemplate.send("shipping-topic", new OrderEvent(event.getOrderId(), OrderStatus.SHIPMENT_REQUESTED));
                break;
            case PAYMENT_FAILED:
                System.out.println("Payment failed. Release Inventory."); // compensating transaction
                kafkaTemplate.send("inventory-topic", new OrderEvent(event.getOrderId(), OrderStatus.INVENTORY_RELEASE));
                break;
            case SHIPMENT_CONFIRMED:
                System.out.println("Order completed successfully: " + event.getOrderId());
                break;
            case FAILED:
                System.out.println("Failure occurred. Triggering compensation for " + event.getOrderId());
                kafkaTemplate.send("compensation-topic", new OrderEvent(event.getOrderId(), OrderStatus.FAILED));
                break;
        }
    }
}

