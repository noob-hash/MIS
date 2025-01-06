package com.example.mis.dto;

import java.util.Map;

import com.example.mis.entity.Inventory;

public class InventoryResponse {
  private Inventory inventory;
  private Map<String, Object> customValues;

  public InventoryResponse(Inventory inventory, Map<String, Object> customValues) {
    this.inventory = inventory;
    this.customValues = customValues;
  }

  // Getters and setters
  public Inventory getInventory() {
    return inventory;
  }

  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }

  public Map<String, Object> getCustomValues() {
    return customValues;
  }

  public void setCustomValues(Map<String, Object> customValues) {
    this.customValues = customValues;
  }
}
