package com.example.mis.dto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDTO {
  private Long id;
  private LocalDate orderDate;
  private String status;
  private String supplierName;
  private String supplierOrganization;
  private String supplierContact;
  private List<OrderItemDetailsDTO> items;
  private Double totalAmount;
  private Date createdDate;
  private Date lastUpdatedDate;
}
