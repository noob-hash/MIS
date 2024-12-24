package com.example.mis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mis.entity.Order;
import com.example.mis.repo.OrderRepo;

@Service
public class OrderService {
    @Autowired
    private OrderRepo orderRepository;

    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long saleId) {
        return orderRepository.findById(saleId);
    }

    public Order createOrUpdateOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Long saleId) {
        orderRepository.deleteById(saleId);
    }
}
