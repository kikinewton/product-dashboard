package com.bsupply.productdashboard.annotation.Validator;

import com.bsupply.productdashboard.annotation.ValidProductInOrder;
import com.bsupply.productdashboard.repository.ProductOrderRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;


public class ProductInOrderValidator implements ConstraintValidator<ValidProductInOrder, UUID> {

  @Autowired
  ProductOrderRepository productOrderRepository;

  @Override
  public void initialize(ValidProductInOrder constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(
          UUID productId, ConstraintValidatorContext constraintValidatorContext) {
    return productOrderRepository.existsByOrderDetailProductId(productId);
  }
}
