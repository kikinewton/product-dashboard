package com.bsupply.productdashboard.dto.response;

import java.time.Instant;
import java.util.UUID;

public record OrderFulfillmentResponse(UUID id,
                                       int quantity,
                                       String createdBy,
                                       Instant createdAt,
                                       ProductResponse product) {
}
