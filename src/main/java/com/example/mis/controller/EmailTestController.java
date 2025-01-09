package com.example.mis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.mis.service.EmailService;
import lombok.Data;

@RestController
@RequestMapping("/mis/test/email")
public class EmailTestController {

  @Autowired
  private EmailService emailService;

  @PostMapping("/send-simple")
  public ResponseEntity<?> testSimpleEmail(@RequestBody EmailRequest request) {
    try {
      emailService.sendSimpleEmail(
          request.getTo(),
          request.getSubject(),
          request.getBody());
      return ResponseEntity.ok("Email sent successfully");
    } catch (Exception e) {
      return ResponseEntity.internalServerError()
          .body("Failed to send email: " + e.getMessage());
    }
  }

  @PostMapping("/send-login-notification")
  public ResponseEntity<?> testLoginNotification(@RequestBody LoginNotificationRequest request) {
    try {
      emailService.sendLoginNotification(
          request.getTo(),
          request.getUsername(),
          request.getUserRole());
      return ResponseEntity.ok("Login notification email sent successfully");
    } catch (Exception e) {
      return ResponseEntity.internalServerError()
          .body("Failed to send login notification: " + e.getMessage());
    }
  }
}

@Data
class EmailRequest {
  private String to;
  private String subject;
  private String body;
}

@Data
class LoginNotificationRequest {
  private String to;
  private String username;
  private String userRole;
}