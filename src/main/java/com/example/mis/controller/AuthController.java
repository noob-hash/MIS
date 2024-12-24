package com.example.mis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mis.dto.LoginDto;
import com.example.mis.entity.User;
import com.example.mis.service.UserService;

@RestController
@RequestMapping("/mis/")
public class AuthController {
    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User login(LoginDto loginDto) {
        User profile = (User) userService.loadUserByUsername(loginDto.getUsername());
        if (passwordEncoder.matches(loginDto.getPassword(), profile.getPassword())) {
            return profile;
        }
        return null;
    }

    public User register(User userData) {
        String hashedPassword = passwordEncoder.encode(userData.getPassword());
        userData.setPassword(hashedPassword);
        return userService.saveUser(userData);
    }
}
