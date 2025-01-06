package com.example.mis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mis.entity.CustomFormData;
import com.example.mis.repo.CustomFormRepo;

import jakarta.persistence.Cacheable;

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
    // Check if form exists with same entityType and formName
    Optional<CustomFormData> existingForm = customFormRepo.findByEntityTypeAndFormName(
        customFormData.getEntityType(),
        customFormData.getFormName());

    if (existingForm.isPresent()) {
      // Update existing form
      CustomFormData form = existingForm.get();
      form.setFormConfig(customFormData.getFormConfig());
      return customFormRepo.save(form);
    }

    // Create new if doesn't exist
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

  // Add specific update method if needed
  public CustomFormData updateForm(Long id, CustomFormData updatedForm) {
    CustomFormData existingForm = customFormRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Form not found with id: " + id));

    existingForm.setEntityType(updatedForm.getEntityType());
    existingForm.setFormName(updatedForm.getFormName());
    existingForm.setFormConfig(updatedForm.getFormConfig());

    return customFormRepo.save(existingForm);
  }

}
