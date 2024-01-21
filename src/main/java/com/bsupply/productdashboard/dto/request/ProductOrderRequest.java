package com.bsupply.productdashboard.dto.request;

import com.bsupply.productdashboard.dto.response.AirlineResponse;
import com.bsupply.productdashboard.dto.response.CustomerResponse;
import com.bsupply.productdashboard.dto.response.ProductResponse;

public record ProductOrderRequest(ProductResponse product,
                                  CustomerResponse customer,
                                  AirlineResponse airline,
                                  int quantity,
                                  String flight) {
}
