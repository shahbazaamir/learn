package com.example.order.service.impl;


import com.example.order.entity.Order;
import com.example.order.repository.OrderRepository;
import com.example.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
    public class OrderServiceCacheImpl implements OrderService {

        @Autowired
        private OrderRepository orderRepository;

        public Order createOrder(Order order) {
            return orderRepository.save(order);
        }

        @Cacheable(value = "orders", key = "#id")
        public Optional<Order> findOrder(String id) {
            return orderRepository.findById(id);
        }

        @CacheEvict(value = "orders", key = "#id")
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
    public List<Order> findOrders() {
        return orderRepository.findAll();
    }

    @CacheEvict(value = "orders", key = "#id")
        public boolean deleteOrder(String id) {
            if (orderRepository.existsById(id)) {
                orderRepository.deleteById(id);
                return true;
            }
            return false;
        }
    }