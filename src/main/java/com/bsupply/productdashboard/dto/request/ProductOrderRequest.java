package com.bsupply.productdashboard.dto.request;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

public record ProductOrderRequest(Set<ProductAndQuantityDto> products,
                                  UUID customerId,
                                  UUID airlineId,
                                  int quantity,
                                  String description,
                                  String flight,
                                  Date requiredDate) {
}

