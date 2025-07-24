package com.example.order.model;


public class OrderEvent {
    private String orderId;
    private OrderStatus status;

    // Constructors
    public OrderEvent() {}
    public OrderEvent(String orderId, OrderStatus status) {
        this.orderId = orderId;
        this.status = status;
    }

    // Getters & Setters
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public OrderStatus getStatus() {
        return status;
    }
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}

