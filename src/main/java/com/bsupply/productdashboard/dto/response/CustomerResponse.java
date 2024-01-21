package com.bsupply.productdashboard.dto.response;

import java.util.UUID;

public record CustomerResponse(UUID id,
                               String name,
                               String description,
                               String location,
                               String email,
                               String phoneNumber) {
}
