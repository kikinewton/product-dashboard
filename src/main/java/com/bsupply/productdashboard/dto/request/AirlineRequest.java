package com.bsupply.productdashboard.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AirlineRequest(@NotBlank String name, String description) {

}
