package com.bsupply.productdashboard.dto.request;

import java.util.List;
import java.util.UUID;

public record OrderFulfillmentRequest(UUID productOrderId, List<OrderDetailRequest> orderDetails) {
}
