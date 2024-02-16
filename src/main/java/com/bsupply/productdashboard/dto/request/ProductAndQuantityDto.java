package com.bsupply.productdashboard.dto.request;

import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record ProductAndQuantityDto(UUID productId, @PositiveOrZero int quantity) {
}
