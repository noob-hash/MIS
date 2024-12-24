package com.example.mis.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mis.entity.Inventory;


@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Long> {

}
