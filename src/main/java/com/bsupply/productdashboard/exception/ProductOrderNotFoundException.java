package com.bsupply.productdashboard.exception;

public class ProductOrderNotFoundException extends RuntimeException {

    public ProductOrderNotFoundException(String productOrderId) {
        super("Product order with id: {} not found".formatted(productOrderId));
    }
}
