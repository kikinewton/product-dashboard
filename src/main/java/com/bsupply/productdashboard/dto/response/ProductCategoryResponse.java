package com.bsupply.productdashboard.dto.response;

import java.util.UUID;

public record ProductCategoryResponse(UUID id,
                                      String name,
                                      String description) {
}
