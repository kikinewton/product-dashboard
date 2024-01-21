package com.bsupply.productdashboard.dto.response;

import java.util.UUID;

public record AirlineResponse(
        UUID id,
        String name,
        String description) {
}
