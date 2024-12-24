package com.example.mis.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mis.entity.Sales;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
    
}
