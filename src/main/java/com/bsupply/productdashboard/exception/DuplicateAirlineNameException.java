package com.bsupply.productdashboard.exception;

public class DuplicateAirlineNameException extends RuntimeException{

    public DuplicateAirlineNameException(String airlineName) {
        super("Airline with name: %s already exists".formatted(airlineName));
    }
}
