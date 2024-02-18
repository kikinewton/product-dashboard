package com.bsupply.productdashboard.dto.response;

import com.bsupply.productdashboard.enums.OrderStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ProductOrderResponse(UUID id,
                                   String description,
                                   String flight,
                                   Instant createdAt,
                                   Instant requiredDate,
                                   CustomerResponse customer,
                                   AirlineResponse airline,
                                   OrderStatus status,
                                   List<OrderDetailResponse> orderDetails,
                                   List<OrderFulfillmentResponse> orderFulfillments) {
}
