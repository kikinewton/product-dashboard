package com.bsupply.productdashboard.controller;

import com.bsupply.productdashboard.common.annotation.IntegrationTest;
import com.bsupply.productdashboard.dto.request.ProductRequest;
import com.bsupply.productdashboard.enums.MeasurementUnits;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void shouldAddProduct() throws Exception {

        UUID productCategoryId = UUID.fromString("473d8a91-3a54-44d9-8454-71a749d5d89f");

        ProductRequest productRequest = new ProductRequest("Mango", "Freshly cut", 240, MeasurementUnits.GRAM, 4, productCategoryId);
        String content = objectMapper.writeValueAsString(productRequest);

        mockMvc.perform(post("/api/v1/products")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void shouldGetProductById() throws Exception {

        String productId = "e9a4b64c-71ab-451a-8aed-b2598b9ff5f1";
        mockMvc.perform(get("/api/v1/products/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mango cuts"))
                .andExpect(jsonPath("$.packWeightInKg").value(0.96));
    }


    @Test
    public void shouldGetAllProducts() throws Exception {

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(2)));
    }


    @Test
    public void shouldGetProductsWithNameLike() throws Exception {

        mockMvc.perform(get("/api/v1/products")
                        .param("productName", "mango"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].name").value("Mango cuts"));
    }

    @Test
    public void shouldDeleteProduct() throws Exception {

        String productId = "e9a4b64c-71ab-451a-8aed-b2598b9ff5f1";
        mockMvc.perform(delete("/api/v1/products/{productId}", productId))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdateProduct() throws Exception {

        String productId = "e9a4b64c-71ab-451a-8aed-b2598b9ff5f1";
        UUID productCategoryId = UUID.fromString("473d8a91-3a54-44d9-8454-71a749d5d89f");

        ProductRequest productRequest = new ProductRequest("Sugarloaf", "Sweets", 240, MeasurementUnits.GRAM, 4, productCategoryId);
        String content = objectMapper.writeValueAsString(productRequest);

        mockMvc.perform(put("/api/v1/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sugarloaf"))
                .andExpect(jsonPath("$.description").value("Sweets"));
    }

}