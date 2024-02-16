package com.bsupply.productdashboard.dto.request;

import com.bsupply.productdashboard.enums.MeasurementUnits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record ProductRequest(@NotBlank String name,
                             String description,
                             @PositiveOrZero int weight,
                             MeasurementUnits measurementUnits,
                             @PositiveOrZero int quantityPerPack,
                             UUID productCategoryId) {
}
