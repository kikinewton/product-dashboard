package com.bsupply.productdashboard.dto.request;

import com.bsupply.productdashboard.entity.ProductCategory;
import com.bsupply.productdashboard.enums.MeasurementUnits;
import jakarta.validation.constraints.NotBlank;

public record ProductRequest(@NotBlank String name,
                             String description,
                             int weight,
                             MeasurementUnits measurementUnits,
                             int quantityPerPack,
                             ProductCategory productCategory) {
}
