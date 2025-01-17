package com.example.mis.entity;

import java.util.Date;

import com.example.mis.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
public class UsersDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String orgName;

    @Column(nullable = false)
    private String contact;

    private String emailAddress;

    private String address;

    @Column(nullable = true)
    private String document;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Lob
    @Column(columnDefinition = "BLOB")
    private String image; // Base64 encoded image data

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedDate;

    @ManyToOne
    @JoinColumn(name = "custom_form_data_id")
    private CustomFormData customFormData;

    private String customValue; // Stores the actual custom field values as JSON

    @PrePersist
    private void onCreate() {
        if (image != null && image.contains(",")) {
            image = image.split(",")[1]; // Store only base64 data
        }
        createdDate = new Date();
        lastUpdatedDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        if (image != null && image.contains(",")) {
            image = image.split(",")[1];
        }
        lastUpdatedDate = new Date();
    }
}
