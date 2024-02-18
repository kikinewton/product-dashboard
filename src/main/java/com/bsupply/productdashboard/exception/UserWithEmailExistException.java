package com.bsupply.productdashboard.exception;

public class UserWithEmailExistException extends RuntimeException {
    public UserWithEmailExistException(String email) {
        super("User with email %s already exist".formatted(email));
    }
}
