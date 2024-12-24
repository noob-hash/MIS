package com.example.mis.entity;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false)
    private ProductType type;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer reorder;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String batchNumber;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String storageConditions;

    @Column(nullable = false)
    private String image;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedDate;

    @PrePersist
    private void onCreate() {
        createdDate = new Date();
        lastUpdatedDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedDate = new Date();
    }

    public enum ProductType{GOODS, SERVICE};
}
