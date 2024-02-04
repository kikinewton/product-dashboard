package com.bsupply.productdashboard.controller.advice;

import com.bsupply.productdashboard.dto.ApiError;
import com.bsupply.productdashboard.exception.DuplicateAirlineNameException;
import com.bsupply.productdashboard.exception.DuplicateCustomerNameException;
import com.bsupply.productdashboard.exception.DuplicateProductCategoryNameException;
import com.bsupply.productdashboard.exception.DuplicateProductNameException;
import com.bsupply.productdashboard.exception.ProductOrderFulfillmentException;
import jakarta.validation.UnexpectedTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = {
            UnexpectedTypeException.class,
            DuplicateAirlineNameException.class,
            DuplicateCustomerNameException.class,
            DuplicateProductNameException.class,
            DuplicateProductCategoryNameException.class,
            ProductOrderFulfillmentException.class
    })
    public ResponseEntity<ApiError> handleInputError(Exception exception) {

        String message = exception.getMessage();
        log.error(message);
        ApiError apiError = new ApiError(message);
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {

        BindingResult bindingResult = exception.getBindingResult();
        final Set<String> errors = new HashSet<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String defaultMessage = fieldError.getDefaultMessage();
            errors.add(defaultMessage);
        }

        String error = String.join(", ", errors);
        ApiError apiError = new ApiError(error);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
