package com.bsupply.productdashboard.factory;

import com.bsupply.productdashboard.dto.response.ProductCategoryResponse;
import com.bsupply.productdashboard.entity.ProductCategory;

public class ProductCategoryResponseFactory {

    
    ProductCategoryResponseFactory() {
    }
    
    public static ProductCategoryResponse getProductCategoryResponse(ProductCategory productCategory) {
        return new ProductCategoryResponse(
                productCategory.getId(),
                productCategory.getName(),
                productCategory.getDescription());
    }
}
