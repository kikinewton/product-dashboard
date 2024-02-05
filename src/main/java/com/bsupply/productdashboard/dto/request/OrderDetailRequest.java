package com.bsupply.productdashboard.dto.request;

import com.bsupply.productdashboard.annotation.ValidProductInOrder;

import java.util.UUID;

public record OrderDetailRequest(
        @ValidProductInOrder(message = "Product '${validatedValue}' not found") UUID productId,
        int quantity) {
}
