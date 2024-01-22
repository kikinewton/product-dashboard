package com.bsupply.productdashboard.dto.request;

import com.bsupply.productdashboard.enums.MeasurementUnits;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ProductRequest(@NotBlank String name,
                             String description,
                             int weight,
                             MeasurementUnits measurementUnits,
                             int quantityPerPack,
                             UUID productCategoryId) {
}
