package com.example.mis.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mis.entity.CustomFormData;
import com.example.mis.service.CustomFormService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/mis/custom-forms")
public class CustomFormController {

  @Autowired
  private CustomFormService service;

  @GetMapping
  public ResponseEntity<List<Map<String, Object>>> getAllCustomForms() {
    try {
      List<CustomFormData> forms = service.getAllCustomForm();
      List<Map<String, Object>> parsedForms = forms.stream()
          .map(form -> {
            try {
              return service.getFormWithParsedConfig(form.getEntityType(), form.getFormName());
            } catch (Exception e) {
              return null;
            }
          })
          .filter(form -> form != null)
          .toList();
      return ResponseEntity.ok(parsedForms);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  // Create or update custom form
  @PostMapping("/save")
  public ResponseEntity<?> createOrUpdateCustomForm(@RequestBody Map<String, Object> formData) {
    try {
      String entityType = (String) formData.get("entityType");
      String formName = (String) formData.get("formName");

      CustomFormData customForm = new CustomFormData();
      customForm.setEntityType(entityType);
      customForm.setFormName(formName);
      customForm.setFormConfig(new ObjectMapper().writeValueAsString(formData.get("formConfig")));

      CustomFormData savedForm = service.saveCustomFormData(customForm);
      return ResponseEntity.ok(service.getFormWithParsedConfig(savedForm.getEntityType(), savedForm.getFormName()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Error processing form: " + e.getMessage());
    }
  }

  @PostMapping("/{entityType}/{formName}/fields")
  public ResponseEntity<?> updateFormFields(
      @PathVariable String entityType,
      @PathVariable String formName,
      @RequestParam String operation,
      @RequestBody List<Map<String, Object>> fields) {
    try {
      CustomFormData updatedForm = service.updateFormFields(entityType, formName, fields, operation);
      return ResponseEntity.ok(service.getFormWithParsedConfig(updatedForm.getEntityType(), updatedForm.getFormName()));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Error updating form fields: " + e.getMessage());
    }
  }

  @GetMapping("/{entityType}/{formName}")
  public ResponseEntity<?> getForm(
      @PathVariable String entityType,
      @PathVariable String formName) {
    try {
      Map<String, Object> form = service.getFormWithParsedConfig(entityType, formName);
      return form != null ? ResponseEntity.ok(form) : ResponseEntity.notFound().build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error retrieving form: " + e.getMessage());
    }
  }

  // Get all custom forms (now filtered by entityType)
  @GetMapping("/{entityType}")
  public ResponseEntity<?> getCustomFormsByEntityType(@PathVariable String entityType) {
    try {
      List<Map<String, Object>> forms = service.getCustomFormsByEntityTypeWithParsedConfig(entityType);
      return ResponseEntity.ok(forms);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error retrieving forms: " + e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteForm(@PathVariable Long id) {
    service.deleteForm(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<String> updateCustomForm(
      @PathVariable Long id,
      @RequestBody Map<String, Object> formData) {
    try {
      String entityType = (String) formData.get("entityType");
      String formName = (String) formData.get("formName");
      ObjectMapper objectMapper = new ObjectMapper();
      String formConfig = objectMapper.writeValueAsString(formData.get("formConfig"));

      CustomFormData customForm = new CustomFormData();
      customForm.setEntityType(entityType);
      customForm.setFormName(formName);
      customForm.setFormConfig(formConfig);

      CustomFormData updatedForm = service.updateForm(id, customForm);
      return ResponseEntity.ok("Form updated successfully!");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Error updating form: " + e.getMessage());
    }
  }

  // @PostMapping("/field")
  // public ResponseEntity<?> updateFormField(
  // @RequestParam String entityType,
  // @RequestParam String formName,
  // @RequestParam String operation,
  // @RequestBody Map<String, Object> fieldData) {
  // try {
  // CustomFormData updatedForm = service.updateFormFields(entityType, formName,
  // fieldData, operation);
  // return ResponseEntity.ok(updatedForm);
  // } catch (Exception e) {
  // return ResponseEntity.badRequest().body("Error updating form field: " +
  // e.getMessage());
  // }
  // }
}
