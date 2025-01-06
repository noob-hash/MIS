package com.example.mis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mis.entity.OrderItem;
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

    // public Orders createOrUpdateOrders(Orders order) {
    // return orderRepository.save(order);
    // }

    public Orders createOrUpdateOrders(Orders order) {
        if (order.getItems() == null) {
            order.setItems(new ArrayList<>());
        }
        // Store the items temporarily
        List<OrderItem> items = new ArrayList<>(order.getItems());

        // Clear the items to avoid persistence issues
        order.setItems(new ArrayList<>());

        // Save the order first
        Orders savedOrder = orderRepository.save(order);

        // Add each item properly
        items.forEach(item -> {
            item.setOrder(savedOrder);
            savedOrder.getItems().add(item);
        });

        // Save the order with items
        return orderRepository.save(savedOrder);
    }

    public void deleteOrders(Long saleId) {
        orderRepository.deleteById(saleId);
    }

    public void deleteOrdersBulk(List<Long> ids) {
        orderRepository.deleteAllById(ids);
    }
}
