package com.bsupply.productdashboard.dto.request;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public record OrderFulfillmentRequest(UUID productOrderId,@Valid List<OrderDetailRequest> orderDetails) {
}
