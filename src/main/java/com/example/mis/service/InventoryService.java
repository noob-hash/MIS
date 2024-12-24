package com.example.mis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mis.entity.Inventory;
import com.example.mis.repo.InventoryRepo;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepo inventoryRepository;

    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id).orElse(null);
    }

    public Inventory saveInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }

    public Inventory updateInventory(Long id, Inventory updatedInventory) {
        Inventory inventory = getInventoryById(id);
        inventory.setName(updatedInventory.getName());
        inventory.setDescription(updatedInventory.getDescription());
        inventory.setSku(updatedInventory.getSku());
        inventory.setUnit(updatedInventory.getUnit());
        inventory.setType(updatedInventory.getType());
        inventory.setStock(updatedInventory.getStock());
        inventory.setPrice(updatedInventory.getPrice());
        inventory.setReorder(updatedInventory.getReorder());
        inventory.setExpiryDate(updatedInventory.getExpiryDate());
        inventory.setManufacturer(updatedInventory.getManufacturer());
        inventory.setBatchNumber(updatedInventory.getBatchNumber());
        inventory.setCategory(updatedInventory.getCategory());
        inventory.setStorageConditions(updatedInventory.getStorageConditions());
        inventory.setImage(updatedInventory.getImage());
        return inventoryRepository.save(inventory);
    }
}
