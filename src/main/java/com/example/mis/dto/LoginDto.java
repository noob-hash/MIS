package com.example.mis.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotBlank(message = "This field is required")
    private String email;

    @NotBlank(message = "This field is required")
    private String username;
    @NotBlank(message = "This field is required")
    private String password;
    private String role;
}
