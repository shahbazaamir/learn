package com.example.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

public class EmployeeListener {

    @KafkaListener(topics = "employee.delete.success")
    public void handleEmployeeDeletionSuccess(String departmentId) {
        System.out.println("Employee deleted. Proceeding to delete department " + departmentId);
        // departmentRepository.deleteById(Long.parseLong(departmentId));
    }

    @KafkaListener(topics = "employee.delete.failed")
    public void handleEmployeeDeletionFailure(String departmentId) {
        System.out.println("Employee deletion failed. Aborting department delete " + departmentId);
    }
}
