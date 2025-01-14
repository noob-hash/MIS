package com.example.mis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDetailsDTO {
  private Long id;
  private Long itemId;
  private String itemName;
  private int quantity;
  private Double unitPrice;
  private Double subtotal;
}
