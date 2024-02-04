package com.bsupply.productdashboard.controller;

import com.bsupply.productdashboard.common.annotation.IntegrationTest;
import com.bsupply.productdashboard.dto.request.ProductAndQuantityDto;
import com.bsupply.productdashboard.dto.request.ProductOrderRequest;
import com.bsupply.productdashboard.enums.OrderStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        ProductAndQuantityDto product = new ProductAndQuantityDto(productId, 10);

        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = localDate.atStartOfDay();

        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        Date nextWeekDate = Date.from(instant);

        ProductOrderRequest productOrderRequest = new ProductOrderRequest(
                Set.of(product), customerId, airlineId, 10, "Fresh mangoes", "", nextWeekDate);
        String content = objectMapper.writeValueAsString(productOrderRequest);

        mockMvc.perform(post("/api/v1/productOrders")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void shouldGetProductOrderById() throws Exception {

        String productOrderId = "d51d3f24-8ad7-43b2-87ac-27b1d03c0a1e";
        mockMvc.perform(get("/api/v1/productOrders/{productOrderId}", productOrderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OrderStatus.PENDING.name()))
                .andExpect(jsonPath("$.customer.id").value("2cd4dcae-3a41-4194-9e0d-0cef9501a5f9"));
    }
}