package com.example.mis.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mis.entity.CustomFormData;
import com.example.mis.repo.CustomFormRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CustomFormService {
  @Autowired
  private CustomFormRepo customFormRepo;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public List<CustomFormData> getAllCustomForm() {
    return customFormRepo.findAll();
  }

  public CustomFormData getCustomFormDataById(Long id) {
    return customFormRepo.findById(id).orElse(null);
  }

  public CustomFormData saveCustomFormData(CustomFormData customFormData) throws JsonProcessingException {
    // Check if form exists with same entityType and formName
    Optional<CustomFormData> existingForm = customFormRepo.findByEntityTypeAndFormName(
        customFormData.getEntityType(),
        customFormData.getFormName());

    if (existingForm.isPresent()) {
      CustomFormData form = existingForm.get();
      // Merge existing fields with new fields
      Map<String, Object> existingConfig = objectMapper.readValue(form.getFormConfig(),
          new TypeReference<Map<String, Object>>() {
          });
      Map<String, Object> newConfig = objectMapper.readValue(customFormData.getFormConfig(),
          new TypeReference<Map<String, Object>>() {
          });

      List<Map<String, Object>> existingFields = (List<Map<String, Object>>) existingConfig.getOrDefault("fields",
          new ArrayList<>());
      List<Map<String, Object>> newFields = (List<Map<String, Object>>) newConfig.getOrDefault("fields",
          new ArrayList<>());

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

  public CustomFormData updateFormFields(String entityType, String formName, List<Map<String, Object>> newFields,
      String operation) throws JsonProcessingException {
    Optional<CustomFormData> existingFormOpt = customFormRepo.findByEntityTypeAndFormName(entityType, formName);
    CustomFormData form = existingFormOpt.orElseGet(() -> {
      CustomFormData newForm = new CustomFormData();
      newForm.setEntityType(entityType);
      newForm.setFormName(formName);
      newForm.setFormConfig("{\"fields\":[]}");
      return newForm;
    });

    Map<String, Object> formConfig = objectMapper.readValue(form.getFormConfig(),
        new TypeReference<Map<String, Object>>() {
        });
    List<Map<String, Object>> existingFields = (List<Map<String, Object>>) formConfig.getOrDefault("fields",
        new ArrayList<>());

    switch (operation.toLowerCase()) {
      case "append":
        // Append new fields while updating existing ones
        Map<String, Map<String, Object>> fieldMap = new HashMap<>();
        existingFields.forEach(field -> fieldMap.put((String) field.get("name"), field));
        newFields.forEach(field -> fieldMap.put((String) field.get("name"), field));
        formConfig.put("fields", new ArrayList<>(fieldMap.values()));
        break;

      case "replace":
        // Replace all fields with new fields
        formConfig.put("fields", newFields);
        break;

      case "delete":
        // Remove specified fields
        Set<String> fieldsToDelete = new HashSet<>();
        newFields.forEach(field -> fieldsToDelete.add((String) field.get("name")));
        existingFields.removeIf(field -> fieldsToDelete.contains(field.get("name")));
        formConfig.put("fields", existingFields);
        break;

      default:
        throw new IllegalArgumentException("Invalid operation: " + operation);
    }

    form.setFormConfig(objectMapper.writeValueAsString(formConfig));
    return customFormRepo.save(form);
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

  public Map<String, Object> getFormWithParsedConfig(String entityType, String formName)
      throws JsonProcessingException {
    Optional<CustomFormData> formOpt = customFormRepo.findByEntityTypeAndFormName(entityType, formName);
    if (formOpt.isPresent()) {
      CustomFormData form = formOpt.get();
      Map<String, Object> result = new HashMap<>();
      result.put("id", form.getId());
      result.put("entityType", form.getEntityType());
      result.put("formName", form.getFormName());
      result.put("formConfig", objectMapper.readValue(form.getFormConfig(), Map.class));
      return result;
    }
    return null;
  }

  public List<Map<String, Object>> getCustomFormsByEntityTypeWithParsedConfig(String entityType)
      throws JsonProcessingException {
    List<CustomFormData> forms = customFormRepo.findByEntityType(entityType);
    List<Map<String, Object>> parsedForms = new ArrayList<>();

    for (CustomFormData form : forms) {
      Map<String, Object> parsedForm = new HashMap<>();
      parsedForm.put("id", form.getId());
      parsedForm.put("entityType", form.getEntityType());
      parsedForm.put("formName", form.getFormName());
      parsedForm.put("formConfig", objectMapper.readValue(form.getFormConfig(), Map.class));
      parsedForms.add(parsedForm);
    }

    return parsedForms;
  }
}
