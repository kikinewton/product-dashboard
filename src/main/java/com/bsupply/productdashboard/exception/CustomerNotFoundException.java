package com.bsupply.productdashboard.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String customerId) {
        super("Customer with id %s not found".formatted(customerId));
    }
}
