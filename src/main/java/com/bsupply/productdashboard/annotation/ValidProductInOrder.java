package com.bsupply.productdashboard.annotation;

import com.bsupply.productdashboard.annotation.Validator.ProductInOrderValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ProductInOrderValidator.class)
@Retention(RUNTIME)
@Target({FIELD, METHOD, PARAMETER})
public @interface ValidProductInOrder {
  String message() default "Product not found";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
