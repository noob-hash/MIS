package com.example.mis.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mis.entity.CustomFormData;
import com.example.mis.entity.UsersDetails;
import com.example.mis.enums.UserRole;
import com.example.mis.repo.CustomFormRepo;
import com.example.mis.repo.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private CustomFormRepo customFormRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UsersDetails user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
    }

    // Admin-specific methods
    public List<UsersDetails> getAllSuppliers() {
        return userRepository.findByRole(UserRole.SUPPLIER);
    }

    public UsersDetails registerSupplier(UsersDetails supplier) {
        if (supplier.getRole() != UserRole.SUPPLIER) {
            throw new IllegalArgumentException("Invalid role for supplier registration");
        }
        supplier.setPassword(passwordEncoder.encode(supplier.getPassword()));
        return saveUserWithCustomData(supplier);
    }

    public UsersDetails updateSupplierProfile(Long supplierId, UsersDetails updatedSupplier) {
        UsersDetails existingSupplier = getUserById(supplierId);
        if (existingSupplier.getRole() != UserRole.SUPPLIER) {
            throw new IllegalArgumentException("User is not a supplier");
        }

        existingSupplier.setName(updatedSupplier.getName());
        existingSupplier.setContact(updatedSupplier.getContact());
        existingSupplier.setEmailAddress(updatedSupplier.getEmailAddress());
        existingSupplier.setAddress(updatedSupplier.getAddress());
        existingSupplier.setDocument(updatedSupplier.getDocument());
        existingSupplier.setImage(updatedSupplier.getImage());
        existingSupplier.setCustomValue(updatedSupplier.getCustomValue());

        return saveUserWithCustomData(existingSupplier);
    }

    public List<UsersDetails> getAllUsers() {
        return userRepository.findAll();
    }

    public UsersDetails getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UsersDetails saveUser(UsersDetails user) {
        return userRepository.save(user);
    }

    public UsersDetails saveUserWithCustomData(UsersDetails user) {
        if (user.getCustomFormData() != null && user.getCustomFormData().getId() != null) {
            CustomFormData customFormData = customFormRepo.findById(user.getCustomFormData().getId())
                    .orElseThrow(() -> new RuntimeException("Custom Form not found"));
            user.setCustomFormData(customFormData);
        }

        if (user.getCustomValue() != null) {
            try {
                Map<String, Object> customValues = objectMapper.readValue(user.getCustomValue(),
                        new TypeReference<Map<String, Object>>() {
                        });
                user.setCustomValue(objectMapper.writeValueAsString(customValues));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to process custom values", e);
            }
        }

        return userRepository.save(user);
    }

    public UsersDetails updateUser(Long userId, UsersDetails updatedUser) {
        UsersDetails user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (user == null) {
            return userRepository.save(updatedUser);
        } else {
            user.setAddress(updatedUser.getAddress());
            user.setContact(updatedUser.getContact());
            user.setUsername(updatedUser.getUsername());
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            user.setName(updatedUser.getName());
            user.setRole(updatedUser.getRole());
            user.setCustomValue(updatedUser.getCustomValue());

            return saveUserWithCustomData(user);
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Get users with a specific role
    public List<UsersDetails> getUsersByRole(String role) {
        UserRole userRole = UserRole.valueOf(role.toUpperCase());
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == userRole)
                .collect(Collectors.toList());
    }
}