package com.example.order.service.impl;



import com.example.order.entity.Order;
import com.example.order.repository.OrderRepository;
import com.example.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> updateOrder(String id, Order updatedOrder) {
        return orderRepository.findById(id).map(existingOrder -> {
            existingOrder.setOrderId(updatedOrder.getOrderId());
            existingOrder.setCustomerId(updatedOrder.getCustomerId());
            existingOrder.setTotalWeight(updatedOrder.getTotalWeight());
            existingOrder.setStatus(updatedOrder.getStatus());
            existingOrder.setOrderDate(updatedOrder.getOrderDate());
            return orderRepository.save(existingOrder);
        });
    }

    @Override
    public Optional<Order> findOrder(String id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> findOrders() {
        return orderRepository.findAll();
    }
    @Override
    public boolean deleteOrder(String id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
