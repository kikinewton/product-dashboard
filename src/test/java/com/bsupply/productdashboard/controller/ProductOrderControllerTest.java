package com.bsupply.productdashboard.controller;

import com.bsupply.productdashboard.common.annotation.IntegrationTest;
import com.bsupply.productdashboard.dto.request.OrderDetailRequest;
import com.bsupply.productdashboard.dto.request.OrderFulfillmentRequest;
import com.bsupply.productdashboard.dto.request.ProductAndQuantityDto;
import com.bsupply.productdashboard.dto.request.ProductOrderRequest;
import com.bsupply.productdashboard.entity.ProductOrder;
import com.bsupply.productdashboard.enums.OrderStatus;
import com.bsupply.productdashboard.repository.ProductOrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class ProductOrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Test
    void addProductOrder() throws Exception {

        UUID productId = UUID.fromString("e9a4b64c-71ab-451a-8aed-b2598b9ff5f1");
        UUID customerId = UUID.fromString("2cd4dcae-3a41-4194-9e0d-0cef9501a5f9");
        UUID airlineId = UUID.fromString("094551bd-881a-474a-b652-44a4cddbf3fb");
        ProductAndQuantityDto product = new ProductAndQuantityDto(productId, 10);

        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = localDate.atStartOfDay();

        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        Date nextWeekDate = Date.from(instant);

        ProductOrderRequest productOrderRequest = new ProductOrderRequest(
                Set.of(product), customerId, airlineId,  "Fresh mangoes", "KL99", nextWeekDate);
        String content = objectMapper.writeValueAsString(productOrderRequest);

        mockMvc.perform(post("/api/v1/productOrders")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateProductOrder() throws Exception {
        UUID productOrderId = UUID.fromString("d51d3f24-8ad7-43b2-87ac-27b1d03c0a1e");
        UUID customerId = UUID.fromString("2cd4dcae-3a41-4194-9e0d-0cef9501a5f9");
        UUID airlineId = UUID.fromString("094551bd-881a-474a-b652-44a4cddbf3fb");

        LocalDate localDate = LocalDate.now().plusDays(7);
        LocalDateTime localDateTime = localDate.atStartOfDay();

        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        Date nextWeekDate = Date.from(instant);

        ProductOrderRequest productOrderRequest = new ProductOrderRequest(
                Collections.emptySet(), customerId, airlineId,  "Fresh cut fruits", "KL99-B21", nextWeekDate);
        String content = objectMapper.writeValueAsString(productOrderRequest);

        mockMvc.perform(put("/api/v1/productOrders/{productOrderId}", productOrderId)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void shouldGetProductOrderById() throws Exception {

        String productOrderId = "d51d3f24-8ad7-43b2-87ac-27b1d03c0a1e";
        mockMvc.perform(get("/api/v1/productOrders/{productOrderId}",
                        productOrderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OrderStatus.PENDING.name()))
                .andExpect(jsonPath("$.customer.id")
                        .value("2cd4dcae-3a41-4194-9e0d-0cef9501a5f9"));
    }

    @Test
    public void shouldGetProductOrderByCustomerId() throws Exception {

        String customerId = "2cd4dcae-3a41-4194-9e0d-0cef9501a5f9";
        mockMvc.perform(get("/api/v1/productOrders/customers/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].status")
                        .value(OrderStatus.PENDING.name()))
                .andExpect(jsonPath("$.data[0].customer.id")
                        .value("2cd4dcae-3a41-4194-9e0d-0cef9501a5f9"));
    }

    @Test
    public void throwErrorWhenOrderDetailsIsEmpty() throws Exception {

        String productOrderId = "d51d3f24-8ad7-43b2-87ac-27b1d03c0a1e";
        OrderFulfillmentRequest orderFulfillmentRequest = new OrderFulfillmentRequest(
                UUID.fromString(productOrderId),
                Collections.emptyList());

        String content = objectMapper.writeValueAsString(orderFulfillmentRequest);
        mockMvc.perform(post("/api/v1/productOrders/{productOrderId}/fulfillment",
                        productOrderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Product order with id %s can not be fulfilled"
                                .formatted(productOrderId)));
    }

    @Test
    public void throwExceptionWhenProductIsNotValidForFulfillment() throws Exception {

        UUID productOrderId = UUID.fromString("d51d3f24-8ad7-43b2-87ac-27b1d03c0a1e");
        UUID productId = UUID.randomUUID();
        OrderDetailRequest orderDetailRequest = new OrderDetailRequest(productId, 10);
        OrderFulfillmentRequest orderFulfillmentRequest = new OrderFulfillmentRequest(
                productOrderId,
                List.of(orderDetailRequest));

        String content = objectMapper.writeValueAsString(orderFulfillmentRequest);

        mockMvc.perform(post("/api/v1/productOrders/{productOrderId}/fulfillment",
                        productOrderId)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Product '%s' not found".formatted(productId)));

    }

    @Test
    public void addFulfillmentAndCompleteOrder() throws Exception {

        UUID productOrderId = UUID.fromString("aebc1f59-3248-421f-b0c4-c26fb5d5f507");
        UUID productId = UUID.fromString("e9a4b64c-71ab-451a-8aed-b2598b9ff5f1");
        OrderDetailRequest orderDetailRequest = new OrderDetailRequest(productId, 10);
        OrderFulfillmentRequest orderFulfillmentRequest = new OrderFulfillmentRequest(
                productOrderId,
                List.of(orderDetailRequest));

        String content = objectMapper.writeValueAsString(orderFulfillmentRequest);
        mockMvc.perform(post("/api/v1/productOrders/{productOrderId}/fulfillment",
                        productOrderId)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<ProductOrder> productOrder = productOrderRepository.findById(productOrderId);
        Assertions.assertTrue(productOrder.isPresent()
                              && OrderStatus.COMPLETED.equals(productOrder.get().getStatus()));
    }

    @Test
    void updateOrderDetail() throws Exception {

        UUID productId = UUID.fromString("e9a4b64c-71ab-451a-8aed-b2598b9ff5f1");
        UUID productOrderId = UUID.fromString("aebc1f59-3248-421f-b0c4-c26fb5d5f507");
        UUID orderDetailId = UUID.fromString("0f7231c8-e551-4278-96be-b9292c1ea130");
        OrderDetailRequest orderDetailRequest = new OrderDetailRequest(productId, 101);

        String content = objectMapper.writeValueAsString(orderDetailRequest);

        mockMvc.perform(put("/api/v1/productOrders/{productOrderId}/orderDetails/{orderDetailId}",
                        productOrderId, orderDetailId)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateOrderFulfillment() throws Exception {

        UUID productId = UUID.fromString("e9a4b64c-71ab-451a-8aed-b2598b9ff5f1");
        UUID productOrderId = UUID.fromString("aebc1f59-3248-421f-b0c4-c26fb5d5f507");
        UUID orderFulfillmentId = UUID.fromString("0f7231c8-e551-4278-96be-b9292c1ea130");
        OrderDetailRequest orderDetailRequest = new OrderDetailRequest(productId, 102);

        String content = objectMapper.writeValueAsString(orderDetailRequest);

        mockMvc.perform(put("/api/v1/productOrders/{productOrderId}/orderFulfillments/{orderFulfillmentId}",
                        productOrderId, orderFulfillmentId)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}