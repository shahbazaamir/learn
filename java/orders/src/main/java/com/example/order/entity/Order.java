package com.example.order.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    private Long orderId;
    private Long customerId;
    private double totalWeight;
    private String status; // e.g., "PENDING", "COMPLETED", "CANCELLED"
    private LocalDate orderDate;

    // Constructors
    public Order() {}

    public Order(String id, Long orderId, Long customerId, double totalWeight, String status, LocalDate orderDate) {
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalWeight = totalWeight;
        this.status = status;
        this.orderDate = orderDate;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    // toString
    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", orderId=" + orderId +
                ", customerId=" + customerId +
                ", totalWeight=" + totalWeight +
                ", status='" + status + '\'' +
                ", orderDate=" + orderDate +
                '}';
    }
}
