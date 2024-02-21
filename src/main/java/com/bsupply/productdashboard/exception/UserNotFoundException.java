package com.bsupply.productdashboard.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userId) {
        super("User with id %s not found".formatted(userId));
    }
}
