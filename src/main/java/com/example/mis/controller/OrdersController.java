package com.example.mis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mis.entity.Orders;
import com.example.mis.service.OrdersService;

@RestController
@RequestMapping("/mis/order")
public class OrdersController {
    @Autowired
    private OrdersService orderService;

    @GetMapping
    public List<Orders> getAllOrderss() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orders> getOrdersById(@PathVariable Long id) {
        return orderService.getOrdersById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public Orders createOrders(@RequestBody Orders order) {
        return orderService.createOrUpdateOrders(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orders> updateOrders(@PathVariable Long id, @RequestBody Orders order) {
        return orderService.getOrdersById(id)
                .map(existingOrders -> {
                    order.setId(existingOrders.getId()); // Assuming the Orders entity has an "id" field
                    Orders updatedOrders = orderService.createOrUpdateOrders(order);
                    return ResponseEntity.ok(updatedOrders);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrders(@PathVariable Long id) {
        if (orderService.getOrdersById(id).isPresent()) {
            orderService.deleteOrders(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/bulk-delete")
    public ResponseEntity<Void> deleteOrdersBulk(@RequestBody List<Long> ids) {
        orderService.deleteOrdersBulk(ids);
        return ResponseEntity.noContent().build();
    }

}
