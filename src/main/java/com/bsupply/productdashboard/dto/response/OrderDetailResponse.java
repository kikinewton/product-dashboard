package com.bsupply.productdashboard.dto.response;

import java.time.Instant;
import java.util.UUID;

public record OrderDetailResponse(
        UUID id,
        ProductResponse product,
        int quantity,
        Instant createdAt) {
}
