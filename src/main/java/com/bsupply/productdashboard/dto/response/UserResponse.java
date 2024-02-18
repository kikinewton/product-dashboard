package com.bsupply.productdashboard.dto.response;

import com.bsupply.productdashboard.enums.Role;

import java.util.UUID;

public record UserResponse(UUID id,
                           String email,
                           String firstName,
                           String lastName,
                           Role role) {
}
