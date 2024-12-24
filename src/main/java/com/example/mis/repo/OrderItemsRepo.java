package com.example.mis.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mis.entity.OrderItem;

@Repository
public interface OrderItemsRepo extends JpaRepository<OrderItem, Long>{
    
}
