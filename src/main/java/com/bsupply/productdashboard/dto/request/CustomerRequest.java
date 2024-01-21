package com.bsupply.productdashboard.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(@NotBlank String name,
                              String description,
                              String location,
                              String email,
                              String phoneNumber) {
}
