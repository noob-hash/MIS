package com.example.mis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.mis.entity.Inventory;
import com.example.mis.entity.Orders;
import com.example.mis.entity.UsersDetails;
import com.example.mis.repo.InventoryRepo;
import com.example.mis.repo.OrdersRepo;
import com.example.mis.repo.UserRepo;

@Service
public class ReportService {
  @Autowired
  private OrdersRepo ordersRepo;

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private InventoryRepo inventoryRepo;

  /**
   * Generate a detailed order report.
   *
   * @return A list of maps containing the report data.
   */
  public List<Map<String, Object>> generateOrderReport() {
    try {
      List<Orders> orders = ordersRepo.findAll();

      return orders.stream().map(order -> {
        Map<String, Object> reportData = new HashMap<>();
        reportData.put("Order ID", order.getId());
        reportData.put("Order Date", order.getOrderDate());
        reportData.put("Last Updated Date", order.getLastUpdatedDate());
        reportData.put("Status", order.getStatus());

        // Fetch supplier details
        UsersDetails supplier = userRepo.findById(order.getSupplierId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Supplier not found for order ID: " + order.getId()));
        reportData.put("Supplier Name", supplier.getName());
        reportData.put("Supplier Organization", supplier.getOrgName());
        reportData.put("Supplier Contact", supplier.getContact());

        // Fetch item details
        List<Map<String, Object>> items = order.getItems().stream().map(item -> {
          Inventory inventory = inventoryRepo.findById(item.getItemId())
              .orElseThrow(() -> new ResponseStatusException(
                  HttpStatus.NOT_FOUND,
                  "Inventory item not found for item ID: " + item.getItemId()));

          Map<String, Object> itemData = new HashMap<>();
          itemData.put("Item Name", inventory.getName());
          itemData.put("Quantity", item.getQuantity());
          itemData.put("Price Per Unit", item.getPrice());
          itemData.put("Total Price", item.getQuantity() * item.getPrice());
          return itemData;
        }).collect(Collectors.toList());
        reportData.put("Items", items);

        // Calculate total amount
        reportData.put("Total Amount", order.getTotalAmount());

        return reportData;
      }).collect(Collectors.toList());
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating order report", e);
    }
  }
}
