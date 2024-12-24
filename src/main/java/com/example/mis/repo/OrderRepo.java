package com.example.mis.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mis.entity.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

}
