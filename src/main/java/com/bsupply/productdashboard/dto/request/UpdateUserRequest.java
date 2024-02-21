package com.bsupply.productdashboard.dto.request;

import com.bsupply.productdashboard.enums.Role;
import jakarta.validation.constraints.Email;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        @Email String email,
        Role role
) {
}
