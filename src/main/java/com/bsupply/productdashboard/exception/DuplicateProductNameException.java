package com.bsupply.productdashboard.exception;

public class DuplicateProductNameException extends RuntimeException{
    public DuplicateProductNameException(String productName) {
        super("Product with name: %s already exists".formatted(productName));
    }
}
