package com.example.mis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitilizer {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner populateData() {
        return args -> {

        };
    }
}
