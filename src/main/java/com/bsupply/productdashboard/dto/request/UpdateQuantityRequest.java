package com.bsupply.productdashboard.dto.request;

import jakarta.validation.constraints.PositiveOrZero;

public record UpdateQuantityRequest(@PositiveOrZero int quantity) {
}
