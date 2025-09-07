package com.example.order.service;

import com.example.order.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    public Order createOrder(Order order) ;

    public Optional<Order> updateOrder(String id, Order updatedOrder) ;

    public Optional<Order> findOrder(String id) ;

    public List<Order> findOrders() ;
    public boolean deleteOrder(String id) ;
}
