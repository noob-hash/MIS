// package com.example.mis.config;

// import com.example.mis.entity.UsersDetails;
// import com.example.mis.repo.UserRepo;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Component;

// import java.util.HashSet;
// import java.util.Set;

// @Component
// public class UserDataInitializer implements CommandLineRunner {

// private final UserRepo usersDetailsRepository;
// private final PasswordEncoder passwordEncoder;

// // Constructor Injection of UserRepo and PasswordEncoder
// public UserDataInitializer(UserRepo usersDetailsRepository, PasswordEncoder
// passwordEncoder) {
// this.usersDetailsRepository = usersDetailsRepository;
// this.passwordEncoder = passwordEncoder;
// }

// @Override
// public void run(String... args) throws Exception {
// // Check if data already exists
// if (usersDetailsRepository.count() == 0) {
// // Initialize admin user with roles
// Set<String> adminRoles = new HashSet<>();
// adminRoles.add("ROLE_ADMIN");
// adminRoles.add("ROLE_SUPPLIER");

// UsersDetails adminUser = new UsersDetails();
// adminUser.setUsername("admin");
// adminUser.setPassword(passwordEncoder.encode("admin123")); // Encrypting the
// // password
// adminUser.setUserRoles(adminRoles);
// adminUser.setName("Admin User");
// adminUser.setContact("1234567890");
// adminUser.setAddress("123 Admin Street");
// adminUser.setDocument("admin-doc123");

// usersDetailsRepository.save(adminUser);

// // Initialize another user with different roles (e.g., customer)
// Set<String> customerRoles = new HashSet<>();
// customerRoles.add("CUSTOMER");

// UsersDetails customerUser = new UsersDetails();
// customerUser.setUsername("customer");
// customerUser.setPassword(passwordEncoder.encode("customer123")); //
// Encrypting the password
// customerUser.setUserRoles(customerRoles);
// customerUser.setName("Customer User");
// customerUser.setContact("0987654321");
// customerUser.setAddress("456 Customer Avenue");
// customerUser.setDocument("customer-doc123");

// usersDetailsRepository.save(customerUser);
// }
// }
// }
