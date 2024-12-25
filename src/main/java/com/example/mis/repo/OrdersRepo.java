package com.example.mis.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mis.entity.Orders;

@Repository
public interface OrdersRepo extends JpaRepository<Orders, Long> {

}
