package com.example.mis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mis.entity.CustomFormData;
import com.example.mis.repo.CustomFormRepo;

@Service
public class CustomFormService {
  @Autowired
  private CustomFormRepo customFormRepo;

  public List<CustomFormData> getAllCustomForm() {
    return customFormRepo.findAll();
  }

  public CustomFormData getCustomFormDataById(Long id) {
    return customFormRepo.findById(id).orElse(null);
  }

  public CustomFormData saveCustomFormData(CustomFormData customFormData) {
    return customFormRepo.save(customFormData);
  }

  public Optional<CustomFormData> getForm(String entityType, String formName) {
    return customFormRepo.findByEntityTypeAndFormName(entityType, formName);
  }

  public List<CustomFormData> getCustomFormsByEntityType(String entityType) {
    return customFormRepo.findByEntityType(entityType);
  }

  public void deleteForm(Long id) {
    customFormRepo.deleteById(id);
  }

}
