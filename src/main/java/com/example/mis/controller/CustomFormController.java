package com.example.mis.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mis.entity.CustomFormData;
import com.example.mis.service.CustomFormService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/mis/custom-forms")
public class CustomFormController {

  @Autowired
  private CustomFormService service;

  // @GetMapping()
  // public List<CustomFormData> getAllCustomForm() {
  // return service.getAllCustomForm();
  // }

  @GetMapping()
  public List<Map<String, Object>> getAllCustomForm() {
    List<CustomFormData> customForms = service.getAllCustomForm();

    ObjectMapper objectMapper = new ObjectMapper();
    List<Map<String, Object>> parsedForms = new ArrayList<>();
    for (CustomFormData form : customForms) {
      try {
        Map<String, Object> parsedForm = new HashMap<>();
        parsedForm.put("id", form.getId());
        parsedForm.put("entityType", form.getEntityType());
        parsedForm.put("formName", form.getFormName());

        // Parse the formConfig JSON string into a Map
        Map<String, Object> formConfig = objectMapper.readValue(form.getFormConfig(), Map.class);
        parsedForm.put("formConfig", formConfig);

        parsedForms.add(parsedForm);
      } catch (Exception e) {
        // Log error if JSON parsing fails
        e.printStackTrace();
      }
    }

    return parsedForms;
    // return service.getAllCustomForm();
  }

  // @PostMapping("/save")
  // public ResponseEntity<CustomFormData> createOrUpdateForm(@RequestBody
  // CustomFormData form) {
  // return ResponseEntity.ok(service.saveCustomFormData(form));
  // }

  // Create or update custom form
  @PostMapping("/save")
  public ResponseEntity<String> createOrUpdateCustomForm(@RequestBody Map<String, Object> formData) {
    try {
      String entityType = (String) formData.get("entityType");
      String formName = (String) formData.get("formName");
      ObjectMapper objectMapper = new ObjectMapper();
      String formConfig = objectMapper.writeValueAsString(formData.get("formConfig"));

      // Check if the form already exists for the given entityType and formName
      Optional<CustomFormData> existingForm = service.getForm(entityType, formName);
      CustomFormData customForm;
      if (existingForm.isPresent()) {
        // If the form exists, update it
        customForm = existingForm.get();
        customForm.setFormConfig(formConfig);
      } else {
        // If the form does not exist, create a new one
        customForm = new CustomFormData();
        customForm.setEntityType(entityType);
        customForm.setFormName(formName);
        customForm.setFormConfig(formConfig);
      }

      // Save or update the form
      service.saveCustomFormData(customForm);
      return ResponseEntity.ok("Form processed successfully!");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Error processing form: " + e.getMessage());
    }
  }

  @GetMapping("/{entityType}/{formName}")
  public ResponseEntity<CustomFormData> getForm(
      @PathVariable String entityType,
      @PathVariable String formName) {
    Optional<CustomFormData> form = service.getForm(entityType, formName);
    return form.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  // Get all custom forms (now filtered by entityType)
  @GetMapping("/{entityType}")
  public ResponseEntity<List<Map<String, Object>>> getCustomFormsByEntityType(@PathVariable String entityType) {
    List<CustomFormData> customForms = service.getCustomFormsByEntityType(entityType);
    ObjectMapper objectMapper = new ObjectMapper();
    List<Map<String, Object>> parsedForms = new ArrayList<>();

    for (CustomFormData form : customForms) {
      try {
        Map<String, Object> parsedForm = new HashMap<>();
        parsedForm.put("id", form.getId());
        parsedForm.put("entityType", form.getEntityType());
        parsedForm.put("formName", form.getFormName());

        // Parse the formConfig JSON string into a Map
        Map<String, Object> formConfig = objectMapper.readValue(form.getFormConfig(), Map.class);
        parsedForm.put("formConfig", formConfig);

        parsedForms.add(parsedForm);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return ResponseEntity.ok(parsedForms);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteForm(@PathVariable Long id) {
    service.deleteForm(id);
    return ResponseEntity.noContent().build();
  }

}
