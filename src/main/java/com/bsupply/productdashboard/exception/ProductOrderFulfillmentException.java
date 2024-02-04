package com.bsupply.productdashboard.exception;

public class ProductOrderFulfillmentException extends RuntimeException {
    public ProductOrderFulfillmentException(String productOrderId) {
        super("Product order with id %s can not be fulfilled".formatted(productOrderId));
    }
}
