package com.example.mis.dto;

import com.example.mis.entity.UsersDetails;
import com.example.mis.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDto {

  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
  private String username;

  @NotBlank(message = "Password is required")
  @Size(min = 8, message = "Password must be at least 8 characters long")
  private String password;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String emailAddress;

  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Address is required")
  private String address;

  @NotNull(message = "Company  is required")
  private String orgName;

  @NotNull(message = "Role is required")
  private UserRole role;

  public UsersDetails toUserDetails() {
    UsersDetails user = new UsersDetails();
    user.setUsername(username);
    user.setPassword(password);
    user.setEmailAddress(emailAddress);
    user.setName(name);
    user.setAddress(address);
    user.setOrgName(orgName);
    user.setRole(role);
    return user;
  }
}
