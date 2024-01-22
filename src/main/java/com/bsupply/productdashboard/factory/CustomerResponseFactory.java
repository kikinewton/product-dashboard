package com.bsupply.productdashboard.factory;

import com.bsupply.productdashboard.dto.response.CustomerResponse;
import com.bsupply.productdashboard.entity.Customer;

public class CustomerResponseFactory {
     CustomerResponseFactory() {
    }
    
    public static CustomerResponse getCustomerResponse(Customer customer) {

        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getDescription(),
                customer.getLocation(),
                customer.getEmail(),
                customer.getPhoneNumber());
    }
}
