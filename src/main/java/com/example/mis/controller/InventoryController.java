package com.example.mis.controller;

import java.util.List;
import java.util.Map;

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

import com.example.mis.dto.InventoryResponse;
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

    @GetMapping("/test/{id}")
    public ResponseEntity<InventoryResponse> getInventoryWithCustomValueById(@PathVariable Long id) {
        Inventory inventory = inventoryService.getInventoryById(id);

        // Retrieve custom values from the JSON string and convert to Map
        Map<String, Object> customValues = null;
        if (inventory.getCustomValue() != null) {
            customValues = inventoryService.getCustomValues(inventory.getCustomValue());
        }

        // Return response with Inventory data and custom values as a JSON object
        InventoryResponse response = new InventoryResponse(inventory, customValues);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    public Inventory createInventory(@RequestBody Inventory inventory) {
        // return inventoryService.saveInventory(inventory);
        return inventoryService.saveInventoryWithCustomValue(inventory);
    }

    @PutMapping("/update/{id}")
    public Inventory updateInventory(@PathVariable Long id, @RequestBody Inventory inventory) {
        // return inventoryService.updateInventory(id, inventory);
        return inventoryService.updateInventoryWithCustomValue(id, inventory);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }

}
