package com.example.mis.dto;

import java.time.LocalDate;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRequest {
  private String name;
  private String description;
  private String sku;
  private String unit;
  private String type;
  private Integer stock;
  private Double price;
  private Integer reorder;
  private LocalDate expiryDate;
  private String manufacturer;
  private String batchNumber;
  private String category;
  private String storageConditions;
  private String image;
  private Long customFormId; // Reference to the CustomFormData
  private Map<String, Object> customValues; // Custom field data
}
