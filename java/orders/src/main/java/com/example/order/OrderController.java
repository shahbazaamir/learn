package com.example.order;

import com.example.order.model.OrderEvent;
import com.example.order.model.OrderStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.kafka.core.KafkaTemplate;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    @PostMapping
    public ResponseEntity<String> placeOrder(  @RequestBody OrderEvent event ) {
        kafkaTemplate.send("order-events", new OrderEvent(event.getOrderId(), OrderStatus.ORDER_PLACED));

        return ResponseEntity.status(201).body("Order created " );
    }
}
