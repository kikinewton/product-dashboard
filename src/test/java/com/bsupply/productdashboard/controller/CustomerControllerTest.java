package com.bsupply.productdashboard.controller;

import com.bsupply.productdashboard.common.annotation.IntegrationTest;
import com.bsupply.productdashboard.dto.request.CustomerRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void shouldAddCustomer() throws Exception {

        CustomerRequest request = new CustomerRequest("M&S", "Food store", "Leeds", "support@sainsbury.com", "00000");
        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/customers")
                        .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void givenCustomerIdShouldReturnCustomer() throws Exception {

        String customerId = "2cd4dcae-3a41-4194-9e0d-0cef9501a5f9";
        mockMvc.perform(get("/api/v1/customers/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sainsbury"));
    }

    @Test
    @WithMockUser
    public void shouldFetchAllCustomers() throws Exception {

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    @WithMockUser
    public void shouldUpdateCustomer() throws Exception {

        String customerId = "2cd4dcae-3a41-4194-9e0d-0cef9501a5f9";
        CustomerRequest request = new CustomerRequest("Tesco", "Food store", "York", "support@sainsbury.com", "00000");
        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(put("/api/v1/customers/{customerId}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tesco"))
                .andExpect(jsonPath("$.location").value("York"));
    }

    @Test
    @WithMockUser
    public void shouldDeleteCustomer() throws Exception {

        String customerId = "2cd4dcae-3a41-4194-9e0d-0cef9501a5f9";
        mockMvc.perform(delete("/api/v1/customers/{customerId}", customerId))
                .andExpect(status().isOk());
    }
}