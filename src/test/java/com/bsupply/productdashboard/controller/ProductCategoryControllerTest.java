package com.bsupply.productdashboard.controller;

import com.bsupply.productdashboard.common.annotation.IntegrationTest;
import com.bsupply.productdashboard.dto.request.ProductCategoryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class ProductCategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldAddProductCategory() throws Exception {

        ProductCategoryRequest productCategoryRequest = new ProductCategoryRequest("Fresh fruits", "Fruits");
        String content = objectMapper.writeValueAsString(productCategoryRequest);
        mockMvc.perform(post("/api/v1/productCategories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFetchProductCategories() throws Exception {

        mockMvc.perform(get("/api/v1/productCategories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    void shouldGetProductCategoryById() throws Exception {

        String productCategoryId = "473d8a91-3a54-44d9-8454-71a749d5d89f";
        mockMvc.perform(
                        get("/api/v1/productCategories/{productCategoryId}",
                                productCategoryId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nuts"));
    }

    @Test
    void shouldThrowErrorWhenProductCategoryNameIsBlank() throws Exception {

        String productCategoryId = "473d8a91-3a54-44d9-8454-71a749d5d89f";
        ProductCategoryRequest productCategoryRequest = new ProductCategoryRequest("", "Fruits");
        String content = objectMapper.writeValueAsString(productCategoryRequest);
        mockMvc.perform(
                        put("/api/v1/productCategories/{productCategoryId}",
                                productCategoryId).contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Provide value for name"));

    }

    @Test
    void shouldUpdateProductCategory() throws Exception {

        String productCategoryId = "473d8a91-3a54-44d9-8454-71a749d5d89f";
        ProductCategoryRequest productCategoryRequest = new ProductCategoryRequest("Dried fruits", "Fruits");
        String content = objectMapper.writeValueAsString(productCategoryRequest);
        mockMvc.perform(
                        put("/api/v1/productCategories/{productCategoryId}",
                                productCategoryId).contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dried fruits"));
    }

    @Test
    void shouldDeleteProductCategory() throws Exception {

        String productCategoryId = "473d8a91-3a54-44d9-8454-71a749d5d89f";
        mockMvc.perform(
                        delete("/api/v1/productCategories/{productCategoryId}",
                                productCategoryId).contentType(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}