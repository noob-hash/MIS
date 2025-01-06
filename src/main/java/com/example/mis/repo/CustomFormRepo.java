package com.example.mis.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mis.entity.CustomFormData;

@Repository
public interface CustomFormRepo extends JpaRepository<CustomFormData, Long> {
  Optional<CustomFormData> findByEntityTypeAndFormName(String entityType, String formName);

  // Method to find CustomFormData by entityType
  List<CustomFormData> findByEntityType(String entityType);
}
