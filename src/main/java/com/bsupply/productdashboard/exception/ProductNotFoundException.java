package com.bsupply.productdashboard.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String productId) {
        super("Product with id: %s not found".formatted(productId));
    }
}
