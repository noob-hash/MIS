package com.example.mis.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mis.dto.LoginDto;
import com.example.mis.dto.OtpRequestDto;
import com.example.mis.dto.OtpVerificationDto;
import com.example.mis.entity.UsersDetails;
import com.example.mis.enums.UserRole;
import com.example.mis.repo.UserRepo;

import jakarta.validation.Valid;
import lombok.Data;

@Service
public class AuthService {

  private static final long OTP_EXPIRY_MINUTES = 5;
  private static final int OTP_LENGTH = 6;

  @Autowired
  private UserRepo userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private EmailService emailService;

  // In production, use Redis or a database
  private final Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();

  @Data
  private static class OtpData {
    private final String otp;
    private final LocalDateTime expiry;
    private final String role;
  }

  public UsersDetails authenticate(@Valid LoginDto loginDto) {
    try {

      UsersDetails user = userRepository.findByUsername(loginDto.getUsername())
          .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
      if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
        throw new BadCredentialsException("Invalid credentials");
      }

      if (!user.getRole().name().equals(loginDto.getRole())) {
        throw new BadCredentialsException("Invalid role for this user");
      }

      // Send login notification
      sendLoginNotification(user);

      return user;
    } catch (BadCredentialsException e) {
      throw new BadCredentialsException("Invalid credentials");
    }

  }

  public UsersDetails register(@Valid UsersDetails userData) {
    // Validate password strength
    if (!isPasswordStrong(userData.getPassword())) {
      throw new IllegalArgumentException(
          "Password must be at least 8 characters long and contain uppercase, lowercase, number, and special character");
    }

    if (userRepository.findByEmailAddress(userData.getEmailAddress()).isPresent()) {
      throw new IllegalArgumentException("Email already exists");
    }

    // Validate unique username
    // if (userRepository.findByUsername(userData.getUsername()).isPresent()) {
    // throw new IllegalArgumentException("Username already exists");
    // }

    // Validate role
    if (userData.getRole() == UserRole.SUPER_ADMIN) {
      throw new IllegalArgumentException("Cannot register as SUPER_ADMIN");
    }

    // Hash password
    userData.setPassword(passwordEncoder.encode(userData.getPassword()));

    // Save user
    UsersDetails savedUser = userRepository.save(userData);

    // Send welcome email
    sendWelcomeEmail(savedUser);

    return savedUser;
  }

  public void sendOtp(@Valid OtpRequestDto request) {
    UsersDetails user = userRepository.findByEmailAddress(request.getEmail())
        .orElseThrow(() -> new BadCredentialsException("User not found"));

    if (!user.getRole().toString().equals(request.getRole())) {
      throw new BadCredentialsException("Invalid role for this user");
    }

    String otp = generateOtp();
    LocalDateTime expiry = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);

    otpStorage.put(request.getEmail(), new OtpData(otp, expiry, request.getRole()));

    emailService.sendSimpleEmail(
        request.getEmail(),
        "Login OTP for Medical Inventory System",
        String.format("Your OTP is: %s\nValid for 5 minutes.", otp, OTP_EXPIRY_MINUTES));
  }

  public UsersDetails verifyOtp(@Valid OtpVerificationDto request) {
    OtpData otpData = otpStorage.get(request.getEmail());

    if (otpData == null) {
      throw new BadCredentialsException("No OTP request found");
    }

    if (LocalDateTime.now().isAfter(otpData.getExpiry())) {
      otpStorage.remove(request.getEmail());
      throw new BadCredentialsException("OTP has expired");
    }

    if (!otpData.getOtp().equals(request.getOtp()) ||
        !otpData.getRole().equals(request.getRole())) {
      throw new BadCredentialsException("Invalid or expired OTP");
    }

    UsersDetails user = userRepository.findByEmailAddress(request.getEmail())
        .orElseThrow(() -> new BadCredentialsException("User not found"));

    // Remove used OTP
    otpStorage.remove(request.getEmail());

    // Send login notification
    sendLoginNotification(user);

    return user;
  }

  private String generateOtp() {
    SecureRandom secureRandom = new SecureRandom();
    StringBuilder otp = new StringBuilder(OTP_LENGTH);
    for (int i = 0; i < OTP_LENGTH; i++) {
      otp.append(secureRandom.nextInt(10));
    }
    return otp.toString();
  }

  private boolean isPasswordStrong(String password) {
    return password != null &&
        password.length() >= 8 &&
        password.matches(".*[A-Z].*") &&
        password.matches(".*[a-z].*") &&
        password.matches(".*\\d.*") &&
        password.matches(".*[!@#$%^&*()\\-_=+\\[\\]{};:,.<>?].*");
  }

  private void sendLoginNotification(UsersDetails user) {
    try {
      emailService.sendLoginNotification(
          user.getEmailAddress(),
          user.getUsername(),
          user.getRole().toString());
    } catch (Exception e) {
      ResponseEntity.badRequest().body("Registration failed: ");
    }
  }

  private void sendWelcomeEmail(UsersDetails user) {
    emailService.sendSimpleEmail(
        user.getEmailAddress(),
        "Welcome to Medical Inventory System",
        String.format("Hello %s,\n\nWelcome to the Medical Inventory System. " +
            "Your account has been created successfully with the role of %s.\n\n" +
            "Best regards,\nMedical Inventory Team",
            user.getName(),
            user.getRole()));
  }
}
