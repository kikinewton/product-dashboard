package com.bsupply.productdashboard.exception;

public class DuplicateCustomerNameException extends RuntimeException{
    public DuplicateCustomerNameException(String customerName) {
        super("Customer with name: %s already exists".formatted(customerName));
    }
}
