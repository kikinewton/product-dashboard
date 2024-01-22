package com.bsupply.productdashboard.factory;

import com.bsupply.productdashboard.dto.response.ProductCategoryResponse;
import com.bsupply.productdashboard.dto.response.ProductResponse;
import com.bsupply.productdashboard.entity.Product;

public class ProductResponseFactory {
     ProductResponseFactory() {
    }
    
    public static ProductResponse getProductResponse(Product product) {

        ProductCategoryResponse productCategoryResponse = ProductCategoryResponseFactory
                .getProductCategoryResponse(product.getProductCategory());

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getQuantityPerPack(),
                product.getMeasurementUnits(),
                product.getPackWeightInKg(),
                productCategoryResponse
        );
    }
}
