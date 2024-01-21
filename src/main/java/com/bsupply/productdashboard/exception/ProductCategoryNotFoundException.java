package com.bsupply.productdashboard.exception;

public class ProductCategoryNotFoundException extends RuntimeException {

    public ProductCategoryNotFoundException(String productCategoryId) {
        super("Product category with id: %s not found".formatted(productCategoryId));
    }
}
