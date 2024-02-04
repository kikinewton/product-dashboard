package com.bsupply.productdashboard.dto.request;

import java.util.UUID;

public record ProductAndQuantityDto(UUID productId, int quantity) {
}
