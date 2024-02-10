package com.bsupply.productdashboard.factory;

import com.bsupply.productdashboard.dto.response.OrderFulfillmentResponse;
import com.bsupply.productdashboard.dto.response.ProductResponse;
import com.bsupply.productdashboard.entity.OrderFulfillment;

public class OrderFulfillmentResponseFactory {

    OrderFulfillmentResponseFactory() {
    }

    public static final OrderFulfillmentResponse getOrderFulfillmentResponse(OrderFulfillment orderFulfillment) {

        ProductResponse productResponse = ProductResponseFactory.getProductResponse(orderFulfillment.getProduct());

        return new OrderFulfillmentResponse(orderFulfillment.getId(),
                orderFulfillment.getQuantity(),
                orderFulfillment.getCreatedBy(),
                orderFulfillment.getCreatedAt(),
                productResponse);
    }

}
