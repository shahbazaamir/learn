package com.example.order.controller;

import java.util.Optional;

import com.example.order.entity.Order;
import com.example.order.model.OrderEvent;
import com.example.order.model.OrderStatus;
import com.example.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.kafka.core.KafkaTemplate;


@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Autowired
    @Qualifier("orderServiceImpl")
    private OrderService orderService;

    @PostMapping("/publish")
    public ResponseEntity<String> placeOrder(  @RequestBody OrderEvent event ) {
        kafkaTemplate.send("order-events", new OrderEvent(event.getOrderId(), OrderStatus.ORDER_PLACED));
        return ResponseEntity.status(201).body("Order created " );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findOrder(@PathVariable String id) {
        Optional<Order> viewOrder = orderService.findOrder(id);
        if (viewOrder.isPresent()) {
            return ResponseEntity.ok(viewOrder.get());
        } else {
            return null;
        }
    }

    @GetMapping
    public ResponseEntity<Object> findOrders() {
       return ResponseEntity.ok(orderService.findOrders());
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.ok(createdOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable String id, @RequestBody Order updatedOrder) {
        return orderService.updateOrder(id, updatedOrder)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable String id) {
        boolean deleted = orderService.deleteOrder(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

