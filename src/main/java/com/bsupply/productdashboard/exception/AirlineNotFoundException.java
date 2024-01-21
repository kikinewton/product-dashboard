package com.bsupply.productdashboard.exception;

public class AirlineNotFoundException extends RuntimeException {
    public AirlineNotFoundException(String airlineId) {
        super("Airline with id: %s not found".formatted(airlineId));
    }
}
