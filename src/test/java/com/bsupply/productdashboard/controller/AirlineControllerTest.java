package com.bsupply.productdashboard.controller;

import com.bsupply.productdashboard.common.annotation.IntegrationTest;
import com.bsupply.productdashboard.dto.request.AirlineRequest;
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
class AirlineControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldCreateAirline() throws Exception {

        AirlineRequest airlineRequest = new AirlineRequest("BTA", "");
        String content = objectMapper.writeValueAsString(airlineRequest);
        mockMvc.perform(post("/api/v1/airlines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());
    }

    @Test
    void shouldThrowAnExceptionWhenCreatingAirlineWithDuplicateName() throws Exception {

        AirlineRequest airlineRequest = new AirlineRequest("KLM", "");
        String content = objectMapper.writeValueAsString(airlineRequest);
        mockMvc.perform(post("/api/v1/airlines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Airline with name: %s already exists".formatted("KLM")));
    }

    @Test
    void shouldFetchAllAirlines() throws Exception {

        mockMvc.perform(get("/api/v1/airlines")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldFetchAirlineById() throws Exception {

        String airlineId = "4ae0dea8-78c8-4595-b24d-65e944067a6a";
        mockMvc.perform(get("/api/v1/airlines/{airlineId}", airlineId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("SAS"));
    }

    @Test
    void shouldUpdateAirline() throws Exception {

        String airlineId = "4ae0dea8-78c8-4595-b24d-65e944067a6a";
        AirlineRequest airlineRequest = new AirlineRequest("Qatar", "Qatar");
        String content = objectMapper.writeValueAsString(airlineRequest);
        mockMvc.perform(put("/api/v1/airlines/{airlineId}", airlineId)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(airlineId))
                .andExpect(jsonPath("$.name").value("Qatar"));
    }

    @Test
    void shouldDeleteAirline() throws Exception {

        String airlineId = "4ae0dea8-78c8-4595-b24d-65e944067a6a";
        mockMvc.perform(delete("/api/v1/airlines/{airlineId}", airlineId))
                .andExpect(status().isOk());
    }

}