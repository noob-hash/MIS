package com.example.mis.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mis.entity.InventoryHistory;

public interface InventoryHistoryRepo extends JpaRepository<InventoryHistory, Long> {
  List<InventoryHistory> findByInventoryIdOrderByModifiedDateDesc(Long inventoryId);

}
