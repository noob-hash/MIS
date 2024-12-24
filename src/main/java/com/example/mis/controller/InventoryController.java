package com.example.mis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mis.entity.Inventory;
import com.example.mis.service.InventoryService;

@RestController
@RequestMapping("/mis/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public List<Inventory> getAllInventorys() {
        return inventoryService.getAllInventories();
    }

    @GetMapping("/{id}")
    public Inventory getInventoryById(@PathVariable Long id) {
        return inventoryService.getInventoryById(id);
    }

    @PostMapping
    public Inventory createInventory(@PathVariable Long id, @RequestBody Inventory inventory) {
        return inventoryService.updateInventory(id,inventory);
    }

    @PutMapping("/{id}")
    public Inventory updateInventory(@PathVariable Long id, @RequestBody Inventory inventory) {
        return inventoryService.getInventoryById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        if (inventoryService.getInventoryById(id)!=null) {
            inventoryService.deleteInventory(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
