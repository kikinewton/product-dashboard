package com.bsupply.productdashboard.dto.response;

import java.util.UUID;

public record ProductOrderResponse(UUID id,
                                   ProductResponse product,
                                   CustomerResponse customer,
                                   AirlineResponse airline,
                                   int quantity,
                                   String flight) {
}
