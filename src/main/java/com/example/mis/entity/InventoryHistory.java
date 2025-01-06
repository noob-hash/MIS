package com.example.mis.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
public class InventoryHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "inventory_id", nullable = false)
  private Inventory inventory;

  @Column(nullable = false)
  private String action; // CREATE, UPDATE, DELETE

  @Column(nullable = false)
  private Integer previousStock;

  @Column(nullable = false)
  private Integer newStock;

  @Column(nullable = false)
  private Double previousPrice;

  @Column(nullable = false)
  private Double newPrice;

  @Lob
  @Column
  private String changes; // Store JSON of all changed fields

  @Column(nullable = false)
  private Date modifiedDate;

  @Column(nullable = false)
  private String modifiedBy; // User who made the change
}
