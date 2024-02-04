package com.bsupply.productdashboard.dto.response;

import java.time.Instant;

public record OrderDetailResponse(ProductResponse product,
                                  int quantity,
                                  Instant createdAt) {
}
