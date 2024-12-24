package com.example.mis.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mis.entity.SalesItem;

@Repository
public interface SalesItemsRepo extends JpaRepository<SalesItem, Long> {
    
}
