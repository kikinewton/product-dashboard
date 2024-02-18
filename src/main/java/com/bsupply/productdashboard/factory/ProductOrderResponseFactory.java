package com.bsupply.productdashboard.factory;

import com.bsupply.productdashboard.dto.response.AirlineResponse;
import com.bsupply.productdashboard.dto.response.CustomerResponse;
import com.bsupply.productdashboard.dto.response.OrderDetailResponse;
import com.bsupply.productdashboard.dto.response.OrderFulfillmentResponse;
import com.bsupply.productdashboard.dto.response.ProductOrderResponse;
import com.bsupply.productdashboard.entity.OrderFulfillment;
import com.bsupply.productdashboard.entity.ProductOrder;

import java.util.List;
import java.util.stream.Collectors;

public class ProductOrderResponseFactory {
    ProductOrderResponseFactory() {
    }

    public static ProductOrderResponse getProductOrderResponse(
            ProductOrder productOrder,
            List<OrderFulfillment> fulfillments) {

        AirlineResponse airlineResponse = AirlineResponseFactory.getAirlineResponse(productOrder.getAirline());

        List<OrderDetailResponse> orderDetailResponse = productOrder.getOrderDetail()
                .stream()
                .map(o -> OrderDetailResponseFactory.getOrderDetailResponse(o))
                .collect(Collectors.toList());


        CustomerResponse customerResponse = CustomerResponseFactory.getCustomerResponse(productOrder.getCustomer());

        List<OrderFulfillmentResponse> orderFulfillmentResponse = fulfillments
                .stream()
                .map(f -> OrderFulfillmentResponseFactory.getOrderFulfillmentResponse(f))
                .collect(Collectors.toList());

        return new ProductOrderResponse(
                productOrder.getId(),
                productOrder.getDescription(),
                productOrder.getFlight(),
                productOrder.getCreatedAt(),
                productOrder.getRequiredDate(),
                customerResponse,
                airlineResponse,
                productOrder.getStatus(),
                orderDetailResponse,
                orderFulfillmentResponse
        );
    }
}
