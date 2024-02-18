package com.bsupply.productdashboard.dto.request;

import com.bsupply.productdashboard.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRegistrationRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Email String email,
        @NotBlank(message = "Enter password") String password,
        Role role) {
}
