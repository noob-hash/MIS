package com.example.mis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mis.dto.LoginDto;
import com.example.mis.dto.OtpRequestDto;
import com.example.mis.dto.OtpVerificationDto;
import com.example.mis.entity.UsersDetails;
import com.example.mis.service.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/mis/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    // @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
    // UsersDetails profile = userService.findByUsername(loginDto.getUsername());

    // if (profile != null &&
    // passwordEncoder.matches(loginDto.getPassword(), profile.getPassword()) &&
    // profile.getRole().toString().equals(loginDto.getRole())) {
    // return ResponseEntity.ok(profile);
    // }

    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
    // .body("Invalid credentials or role");
    // }

    // @PostMapping("/register")
    // public ResponseEntity<?> register(@RequestBody UsersDetails userData) {
    // try {
    // String hashedPassword = passwordEncoder.encode(userData.getPassword());
    // userData.setPassword(hashedPassword);
    // UsersDetails savedUser = userService.saveUser(userData);
    // return ResponseEntity.ok(savedUser);
    // } catch (Exception e) {
    // return ResponseEntity.badRequest()
    // .body("Registration failed: " + e.getMessage());
    // }
    // }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            UsersDetails user = authService.authenticate(loginDto);
            return ResponseEntity.ok(user);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsersDetails userData) {
        try {
            UsersDetails registeredUser = authService.register(userData);
            return ResponseEntity.ok(registeredUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@Valid @RequestBody OtpRequestDto request) {
        try {
            authService.sendOtp(request);
            return ResponseEntity.ok("OTP sent successfully.");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody OtpVerificationDto request) {
        try {
            UsersDetails user = authService.verifyOtp(request);
            return ResponseEntity.ok(user);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
