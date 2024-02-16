package com.bsupply.productdashboard.dto.request;

import jakarta.validation.constraints.FutureOrPresent;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

public record ProductOrderRequest(Set<ProductAndQuantityDto> products,
                                  UUID customerId,
                                  UUID airlineId,
                                  String description,
                                  String flight,
                                  @FutureOrPresent Date requiredDate) {
}

