package com.bsupply.productdashboard.controller;

import com.bsupply.productdashboard.dto.request.AirlineRequest;
import com.bsupply.productdashboard.dto.response.AirlineResponse;
import com.bsupply.productdashboard.service.AirlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/airlines")
@RequiredArgsConstructor
public class AirlineController {

    private final AirlineService airlineService;

    @PostMapping
    public ResponseEntity<Void> createAirline(
            @RequestBody AirlineRequest airlineRequest) {

        airlineService.addAirline(airlineRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AirlineResponse>> getAirlines() {

        List<AirlineResponse> airlines = airlineService.getAirlines();
        return ResponseEntity.ok(airlines);
    }

    @GetMapping("/{airlineId}")
    public ResponseEntity<AirlineResponse> getAirlineById(
            @PathVariable(name = "airlineId") UUID airlineId) {

        AirlineResponse airline = airlineService.getAirlineById(airlineId);
        return ResponseEntity.ok(airline);
    }

    @PutMapping("/{airlineId}")
    public ResponseEntity<AirlineResponse> updateAirline(
            @PathVariable(name = "airlineId") UUID airlineId,
            @RequestBody AirlineRequest airlineRequest
    ) {

        AirlineResponse airline = airlineService.updateAirline(airlineId, airlineRequest);
        return ResponseEntity.ok(airline);
    }

    @DeleteMapping("/{airlineId}")
    public ResponseEntity<Void> deleteAirline(@PathVariable(name = "airlineId") UUID airlineId) {

        airlineService.deleteAirline(airlineId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
