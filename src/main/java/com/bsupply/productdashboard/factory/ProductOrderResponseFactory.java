package com.bsupply.productdashboard.factory;

import com.bsupply.productdashboard.dto.response.AirlineResponse;
import com.bsupply.productdashboard.dto.response.CustomerResponse;
import com.bsupply.productdashboard.dto.response.ProductOrderResponse;
import com.bsupply.productdashboard.dto.response.ProductResponse;
import com.bsupply.productdashboard.entity.ProductOrder;

public class ProductOrderResponseFactory {
    ProductOrderResponseFactory() {
    }

    public static ProductOrderResponse getProductOrderResponse(ProductOrder productOrder) {

        AirlineResponse airlineResponse = AirlineResponseFactory.getAirlineResponse(productOrder.getAirline());
        ProductResponse productResponse = ProductResponseFactory.getProductResponse(productOrder.getProduct());
        CustomerResponse customerResponse = CustomerResponseFactory.getCustomerResponse(productOrder.getCustomer());

        return new ProductOrderResponse(
                productOrder.getId(),
                productResponse,
                customerResponse,
                airlineResponse,
                productOrder.getQuantity(),
                productOrder.getFlight()
        );
    }
}
