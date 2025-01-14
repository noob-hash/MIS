package com.example.mis.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.mis.entity.Orders;

@Repository
public interface OrdersRepo extends JpaRepository<Orders, Long> {

  @Query("SELECT o FROM Orders o WHERE o.status = :status")
  List<Orders> findByStatus(String status);

}
