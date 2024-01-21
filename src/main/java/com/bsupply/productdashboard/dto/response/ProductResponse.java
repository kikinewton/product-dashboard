package com.bsupply.productdashboard.dto.response;

import com.bsupply.productdashboard.enums.MeasurementUnits;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ProductResponse(UUID id,
                              @NotBlank String name,
                              String description,
                              int quantityPerPack,
                              MeasurementUnits measurementUnits,
                              double packWeightInKg,
                              ProductCategoryResponse productCategory) {
}
