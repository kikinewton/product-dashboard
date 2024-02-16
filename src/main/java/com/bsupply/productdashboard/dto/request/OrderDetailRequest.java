package com.bsupply.productdashboard.dto.request;

import com.bsupply.productdashboard.annotation.ValidProductInOrder;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record OrderDetailRequest(
        @ValidProductInOrder(message = "Product '${validatedValue}' not found") UUID productId,
        @PositiveOrZero int quantity) {
}
