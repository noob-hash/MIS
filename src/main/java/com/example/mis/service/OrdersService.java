package com.example.mis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mis.entity.Orders;
import com.example.mis.repo.OrdersRepo;

@Service
public class OrdersService {
    @Autowired
    private OrdersRepo orderRepository;

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Orders> getOrdersById(Long saleId) {
        return orderRepository.findById(saleId);
    }

    public Orders createOrUpdateOrders(Orders order) {
        return orderRepository.save(order);
    }

    public void deleteOrders(Long saleId) {
        orderRepository.deleteById(saleId);
    }
}
