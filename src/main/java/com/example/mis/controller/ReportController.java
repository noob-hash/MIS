package com.example.mis.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mis.service.ReportService;

@RestController
@RequestMapping("/mis/reports")
public class ReportController {
  @Autowired
  private ReportService reportService;

  /**
   * Endpoint to fetch the order report.
   *
   * @return List of report data.
   */
  @GetMapping("/orders")
  public ResponseEntity<List<Map<String, Object>>> getOrderReport() {
    List<Map<String, Object>> report = reportService.generateOrderReport();
    return ResponseEntity.ok(report);
  }
}
