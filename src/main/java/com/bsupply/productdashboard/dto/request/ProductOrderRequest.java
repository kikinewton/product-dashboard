package com.bsupply.productdashboard.dto.request;

import java.util.UUID;

public record ProductOrderRequest(UUID productId,
                                  UUID customerId,
                                  UUID airlineId,
                                  int quantity,
                                  String description,
                                  String flight) {
}
