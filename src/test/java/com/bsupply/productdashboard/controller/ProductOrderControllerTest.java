package com.bsupply.productdashboard.controller;

import com.bsupply.productdashboard.common.annotation.IntegrationTest;
import com.bsupply.productdashboard.dto.request.ProductOrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class ProductOrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void addProductOrder() throws Exception {

        UUID productId = UUID.fromString("e9a4b64c-71ab-451a-8aed-b2598b9ff5f1");
        UUID customerId = UUID.fromString("2cd4dcae-3a41-4194-9e0d-0cef9501a5f9");
        UUID airlineId = UUID.fromString("094551bd-881a-474a-b652-44a4cddbf3fb");
        ProductOrderRequest productOrderRequest = new ProductOrderRequest(productId, customerId, airlineId, 10, "Fresh mangoes", "");
        String content = objectMapper.writeValueAsString(productOrderRequest);

        mockMvc.perform(post("/api/v1/productOrders")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}