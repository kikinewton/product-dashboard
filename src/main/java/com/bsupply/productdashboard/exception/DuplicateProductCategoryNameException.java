package com.bsupply.productdashboard.exception;

public class DuplicateProductCategoryNameException extends RuntimeException{
    public DuplicateProductCategoryNameException(String productCategoryName) {
        super("Product category with name: %s already exists".formatted(productCategoryName));
    }
}
