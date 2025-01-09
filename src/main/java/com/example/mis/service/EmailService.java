package com.example.mis.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

  @Autowired
  private JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String fromEmail;

  public void sendLoginNotification(String to, String username, String userRole) {
    String subject = "New Login Detected - Inventory Management System";

    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    String body = String.format("""
        Dear %s,

        A new login was detected for your account:

        Username: %s
        Role: %s
        Time: %s

        If this wasn't you, please contact the system administrator immediately.

        Best regards,
        Inventory Management Team
        """, username, username, userRole, now.format(formatter));

    sendSimpleEmail(to, subject, body);
  }

  public void sendSimpleEmail(String to, String subject, String body) {
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom(fromEmail);
      message.setTo(to);
      message.setSubject(subject);
      message.setText(body);
      mailSender.send(message);
    } catch (Exception e) {
      // Log the error but don't throw it to prevent login process disruption
      System.err.println("Failed to send email: " + e.getMessage());
    }
  }

  public void sendHtmlEmail(String to, String subject, String htmlContent) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      helper.setFrom(fromEmail);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(htmlContent, true);

      mailSender.send(message);
    } catch (MessagingException e) {
      System.err.println("Failed to send HTML email: " + e.getMessage());
    }
  }

}