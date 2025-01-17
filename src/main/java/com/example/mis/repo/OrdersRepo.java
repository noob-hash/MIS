package com.example.mis.repo;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.mis.entity.Orders;

@Repository
public interface OrdersRepo extends JpaRepository<Orders, Long> {

  @Query("SELECT o FROM Orders o WHERE o.status = :status")
  List<Orders> findByStatus(String status);

  @Query(value = "SELECT orders.id, users_details.name, " +
      "(SELECT SUM(order_item.price) FROM order_item WHERE order_item.order_id = orders.id) AS total_amount, " +
      "DATE(orders.last_updated_date) AS last_purchase_date " +
      "FROM orders " +
      "JOIN users_details ON users_details.id = orders.supplier_id", nativeQuery = true)
  List<Map<String, Object>> getOrderSummary();

}
