package com.bsupply.productdashboard.factory;

import com.bsupply.productdashboard.dto.response.AirlineResponse;
import com.bsupply.productdashboard.dto.response.CustomerResponse;
import com.bsupply.productdashboard.dto.response.OrderDetailResponse;
import com.bsupply.productdashboard.dto.response.ProductOrderResponse;
import com.bsupply.productdashboard.entity.ProductOrder;

import java.util.List;
import java.util.stream.Collectors;

public class ProductOrderResponseFactory {
    ProductOrderResponseFactory() {
    }

    public static ProductOrderResponse getProductOrderResponse(ProductOrder productOrder) {

        AirlineResponse airlineResponse = AirlineResponseFactory.getAirlineResponse(productOrder.getAirline());

        List<OrderDetailResponse> orderDetailResponse = productOrder.getOrderDetail()
                .stream()
                .map(o -> OrderDetailResponseFactory.getOrderDetailResponse(o))
                .collect(Collectors.toList());


        CustomerResponse customerResponse = CustomerResponseFactory.getCustomerResponse(productOrder.getCustomer());

        return new ProductOrderResponse(
                productOrder.getId(),
                customerResponse,
                airlineResponse,
                productOrder.getStatus(),
                orderDetailResponse
        );
    }
}
