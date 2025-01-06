package com.example.mis.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mis.dto.InventoryRequest;
import com.example.mis.entity.CustomFormData;
import com.example.mis.entity.Inventory;
import com.example.mis.entity.Inventory.ProductType;
import com.example.mis.entity.InventoryHistory;
import com.example.mis.repo.CustomFormRepo;
import com.example.mis.repo.InventoryHistoryRepo;
import com.example.mis.repo.InventoryRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepo inventoryRepository;

    @Autowired
    private CustomFormRepo customFormRepo;

    @Autowired
    private InventoryHistoryRepo historyRepo;

    @Autowired
    private ObjectMapper objectMapper; // Injected ObjectMapper

    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found for ID: " + id));
    }

    public Inventory saveInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public Inventory saveInventoryWithCustomValue(Inventory inventory) {
        if (inventory.getCustomFormData() != null && inventory.getCustomFormData().getId() != null) {
            CustomFormData customFormData = customFormRepo.findById(inventory.getCustomFormData().getId())
                    .orElseThrow(() -> new RuntimeException("Custom Form not found"));
            inventory.setCustomFormData(customFormData);
        }
        if (inventory.getCustomValue() != null) {
            try {

                Map<String, Object> customValues = objectMapper.readValue(inventory.getCustomValue(),
                        new TypeReference<Map<String, Object>>() {
                        });
                inventory.setCustomValue(objectMapper.writeValueAsString(customValues));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to process custom values", e);
            }
        }

        return inventoryRepository.save(inventory);
    }

    public void deleteInventory(Long id) {
        if (!inventoryRepository.existsById(id)) {
            throw new RuntimeException("Inventory not found for ID: " + id);
        }
        inventoryRepository.deleteById(id);
    }

    public Inventory createInventory(InventoryRequest inventoryRequest) {
        Inventory inventory = new Inventory();

        // Populate standard fields
        inventory.setName(inventoryRequest.getName());
        inventory.setDescription(inventoryRequest.getDescription());
        inventory.setSku(inventoryRequest.getSku());
        inventory.setUnit(inventoryRequest.getUnit());
        inventory.setType(ProductType.valueOf(inventoryRequest.getType().toUpperCase()));
        inventory.setStock(inventoryRequest.getStock());
        inventory.setPrice(inventoryRequest.getPrice());
        inventory.setReorder(inventoryRequest.getReorder());
        inventory.setExpiryDate(inventoryRequest.getExpiryDate());
        inventory.setManufacturer(inventoryRequest.getManufacturer());
        inventory.setBatchNumber(inventoryRequest.getBatchNumber());
        inventory.setCategory(inventoryRequest.getCategory());
        inventory.setStorageConditions(inventoryRequest.getStorageConditions());
        inventory.setImage(inventoryRequest.getImage());

        // Handle custom form data
        if (inventoryRequest.getCustomFormId() != null) {
            CustomFormData customFormData = customFormRepo.findById(inventoryRequest.getCustomFormId())
                    .orElseThrow(() -> new RuntimeException("Custom Form not found"));
            inventory.setCustomFormData(customFormData);

            // Convert custom field values to JSON and store in customValue
            try {
                String customValueJson = objectMapper.writeValueAsString(inventoryRequest.getCustomValues());
                inventory.setCustomValue(customValueJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to process custom form values", e);
            }
        }

        return inventoryRepository.save(inventory);
    }

    public String convertCustomFieldsToJson(CustomFormData customFormData) {
        try {
            return objectMapper.writeValueAsString(customFormData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting custom fields to JSON", e);
        }
    }

    public Map<String, Object> getCustomValues(String customValueJson) {
        if (customValueJson == null || customValueJson.isEmpty()) {
            return new HashMap<>();
        }

        try {

            // Convert the stored JSON string back into a Map
            return objectMapper.readValue(customValueJson, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing custom values JSON", e);
        }
    }

    public Inventory updateInventoryWithCustomValue(Long id, Inventory updatedInventory) {
        Inventory existingInventory = getInventoryById(id);

        existingInventory.setName(updatedInventory.getName());
        existingInventory.setDescription(updatedInventory.getDescription());
        existingInventory.setSku(updatedInventory.getSku());
        existingInventory.setUnit(updatedInventory.getUnit());
        existingInventory.setType(updatedInventory.getType());
        existingInventory.setStock(updatedInventory.getStock());
        existingInventory.setPrice(updatedInventory.getPrice());
        existingInventory.setReorder(updatedInventory.getReorder());
        existingInventory.setExpiryDate(updatedInventory.getExpiryDate());
        existingInventory.setManufacturer(updatedInventory.getManufacturer());
        existingInventory.setBatchNumber(updatedInventory.getBatchNumber());
        existingInventory.setCategory(updatedInventory.getCategory());
        existingInventory.setStorageConditions(updatedInventory.getStorageConditions());
        existingInventory.setImage(updatedInventory.getImage());

        if (updatedInventory.getCustomFormData() != null) {
            String customValueJson = convertCustomFieldsToJson(updatedInventory.getCustomFormData());
            existingInventory.setCustomValue(customValueJson);
        }

        return inventoryRepository.save(existingInventory);
    }

    public Inventory updateInventory(Long id, Inventory updatedInventory) {
        Inventory inventory = getInventoryById(id);
        if (inventory != null) {
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
        return saveInventory(updatedInventory);
    }

    // Helper method to track inventory changes
    private void trackInventoryChange(Inventory oldInventory, Inventory newInventory, String action,
            String modifiedBy) {
        InventoryHistory history = new InventoryHistory();
        history.setInventory(newInventory);
        history.setAction(action);
        history.setPreviousStock(oldInventory != null ? oldInventory.getStock() : 0);
        history.setNewStock(newInventory.getStock());
        history.setPreviousPrice(oldInventory != null ? oldInventory.getPrice() : 0.0);
        history.setNewPrice(newInventory.getPrice());
        history.setModifiedDate(new Date());
        history.setModifiedBy(modifiedBy);

        // Track all changed fields
        if (oldInventory != null) {
            Map<String, Object> changes = new HashMap<>();
            if (!Objects.equals(oldInventory.getName(), newInventory.getName())) {
                changes.put("name", Map.of("old", oldInventory.getName(), "new", newInventory.getName()));
            }
            // Add other field comparisons...

            try {
                history.setChanges(objectMapper.writeValueAsString(changes));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize changes", e);
            }
        }

        historyRepo.save(history);
    }

}
