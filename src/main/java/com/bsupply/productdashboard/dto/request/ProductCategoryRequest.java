package com.bsupply.productdashboard.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ProductCategoryRequest(@NotBlank(message = "Provide value for name") String name, String description) {
}
