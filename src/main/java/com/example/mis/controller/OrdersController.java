package com.example.mis.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mis.dto.ApiResponse;
import com.example.mis.dto.OrderDetailsDTO;
import com.example.mis.entity.Orders;
import com.example.mis.service.OrdersService;

@RestController
@RequestMapping("/mis/order")
public class OrdersController {
    @Autowired
    private OrdersService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Orders>>> getAllOrders() {
        List<Orders> orders = orderService.getAllOrders();
        return ResponseEntity.ok(new ApiResponse<>(true, "Orders retrieved successfully", orders));
    }

    @GetMapping("/details")
    public ResponseEntity<ApiResponse<List<OrderDetailsDTO>>> getAllOrdersWithDetails() {
        List<OrderDetailsDTO> orders = orderService.getAllOrdersWithDetails();
        return ResponseEntity.ok(new ApiResponse<>(true, "Order details retrieved successfully", orders));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<OrderDetailsDTO>>> getPendingOrders() {
        List<OrderDetailsDTO> orders = orderService.getPendingOrders();
        return ResponseEntity.ok(new ApiResponse<>(true, "Pending orders retrieved successfully", orders));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Orders>> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        Orders updatedOrder = orderService.updateOrderStatus(id, statusUpdate.get("status"));
        return ResponseEntity.ok(new ApiResponse<>(true, "Order status updated successfully", updatedOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Orders>> getOrderById(@PathVariable Long id) {
        Orders order = orderService.getOrdersById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Order retrieved successfully", order));
    }

    @GetMapping("/ordersReport")
    public ResponseEntity<List<Map<String, Object>>> getOrderReport() {
        List<Map<String, Object>> report = orderService.generateOrderReport();
        return ResponseEntity.ok(report);
    }

    @PostMapping("/save")
    public Orders createOrders(@RequestBody Orders order) {
        return orderService.createOrUpdateOrders(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Orders>> updateOrder(@PathVariable Long id,
            @Validated @RequestBody Orders order) {
        Orders existingOrder = orderService.getOrdersById(id);
        order.setId(existingOrder.getId());
        Orders updatedOrder = orderService.createOrUpdateOrders(order);
        return ResponseEntity.ok(new ApiResponse<>(true, "Order updated successfully", updatedOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long id) {
        orderService.getOrdersById(id); // Verify existence
        orderService.deleteOrders(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Order deleted successfully", null));
    }

    @DeleteMapping("/bulk-delete")
    public ResponseEntity<Void> deleteOrdersBulk(@RequestBody List<Long> ids) {
        orderService.deleteOrdersBulk(ids);
        return ResponseEntity.noContent().build();
    }

}
