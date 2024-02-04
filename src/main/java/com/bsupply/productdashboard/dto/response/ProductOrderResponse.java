package com.bsupply.productdashboard.dto.response;

import com.bsupply.productdashboard.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

public record ProductOrderResponse(UUID id,
                                   CustomerResponse customer,
                                   AirlineResponse airline,
                                   OrderStatus status,
                                   List<OrderDetailResponse> orderDetails) {
}
