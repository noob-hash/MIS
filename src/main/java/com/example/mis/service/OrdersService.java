package com.example.mis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.mis.dto.OrderDetailsDTO;
import com.example.mis.dto.OrderItemDetailsDTO;
import com.example.mis.entity.Inventory;
import com.example.mis.entity.OrderItem;
import com.example.mis.entity.Orders;
import com.example.mis.entity.UsersDetails;
import com.example.mis.repo.InventoryRepo;
import com.example.mis.repo.OrdersRepo;
import com.example.mis.repo.UserRepo;

@Service
public class OrdersService {
    @Autowired
    private OrdersRepo orderRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private InventoryRepo inventoryRepository;

    public List<OrderDetailsDTO> getAllOrdersWithDetails() {
        try {
            List<Orders> orders = orderRepository.findAll();
            return orders.stream()
                    .map(this::convertToOrderDetailsDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching orders");
        }
    }

    private OrderDetailsDTO convertToOrderDetailsDTO(Orders order) {
        UsersDetails supplier = userRepository.findById(order.getSupplierId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Supplier with ID %d not found", order.getSupplierId())));

        List<OrderItemDetailsDTO> itemDetails = order.getItems().stream()
                .map(this::convertToOrderItemDetailDTO)
                .collect(Collectors.toList());

        return new OrderDetailsDTO(
                order.getId(),
                order.getOrderDate(),
                order.getStatus(),
                supplier.getName(),
                supplier.getOrgName(),
                supplier.getContact(),
                itemDetails,
                order.getTotalAmount(),
                order.getCreatedDate(),
                order.getLastUpdatedDate());
    }

    private OrderItemDetailsDTO convertToOrderItemDetailDTO(OrderItem item) {
        Inventory inventory = inventoryRepository.findById(item.getItemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Inventory item with ID %d not found", item.getItemId())));

        return new OrderItemDetailsDTO(
                item.getId(),
                item.getItemId(),
                inventory.getName(),
                item.getQuantity(),
                item.getPrice(),
                item.getQuantity() * item.getPrice());
    }

    public List<Orders> getAllOrders() {
        try {
            return orderRepository.findAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching orders");
        }
    }

    public Orders getOrdersById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Order with ID %d not found", orderId)));
    }

    public List<OrderDetailsDTO> getPendingOrders() {
        try {
            List<Orders> orders = orderRepository.findByStatus("PENDING");
            return orders.stream()
                    .map(this::convertToOrderDetailsDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching pending orders");
        }
    }

    public Orders updateOrderStatus(Long orderId, String status) {
        Orders order = getOrdersById(orderId);
        order.setStatus(status);
        order.setLastUpdatedDate(new Date());
        return orderRepository.save(order);
    }


    public OrderDetailsDTO getOrderDetailsById(Long orderId) {
        Orders order = getOrdersById(orderId);
        return convertToOrderDetailsDTO(order);
    }

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

    public void deleteOrders(Long orderId) {
        try {
            orderRepository.deleteById(orderId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting order");
        }
    }

    public void deleteOrdersBulk(List<Long> ids) {
        orderRepository.deleteAllById(ids);
    }
}
