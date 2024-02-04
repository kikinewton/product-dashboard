package com.bsupply.productdashboard.dto.request;

import java.util.UUID;

public record OrderDetailRequest(UUID productId, int quantity) {
}
