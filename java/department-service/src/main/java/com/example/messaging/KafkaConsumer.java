package com.example.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void listen(String message) {
        System.out.println("Received: " + message);
    }

    @KafkaListener(topics = "department.delete.request")
    public void onDepartmentDelete(String departmentId) {
        try {
            // Simulate employee deletion
            System.out.println("Deleting employees for department " + departmentId);
            // employeeRepository.deleteByDepartmentId(Long.parseLong(departmentId));
            // simulate delay
            Thread.sleep(1000);
            kafkaTemplate.send("employee.delete.success", departmentId);
        } catch (Exception e) {
            kafkaTemplate.send("employee.delete.failed", departmentId);
        }
    }
}
