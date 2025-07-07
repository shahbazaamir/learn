package com.example.employee.controller;

import org.springframework.web.bind.annotation.*;
import com.example.messaging.KafkaProducer;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    private final KafkaProducer  producer;

    public KafkaController(KafkaProducer  producer) {
        this.producer = producer;
    }

    @PostMapping("/publish")
    public String sendMessage(@RequestParam String message) {
        producer.sendMessage("my-topic", message);
        return "Message sent!";
    }
}
