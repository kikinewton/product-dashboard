package com.bsupply.productdashboard.factory;

import com.bsupply.productdashboard.dto.response.OrderDetailResponse;
import com.bsupply.productdashboard.dto.response.ProductResponse;
import com.bsupply.productdashboard.entity.OrderDetail;

public class OrderDetailResponseFactory {

    OrderDetailResponseFactory() {
    }

    public static OrderDetailResponse getOrderDetailResponse(OrderDetail orderDetail) {

        ProductResponse productResponse = ProductResponseFactory.getProductResponse(orderDetail.getProduct());
        return new OrderDetailResponse(
                productResponse,
                orderDetail.getQuantity(),
                orderDetail.getCreatedAt());
    }

}
