package com.bsupply.productdashboard.service;

import com.bsupply.productdashboard.dto.request.AirlineRequest;
import com.bsupply.productdashboard.dto.response.AirlineResponse;
import com.bsupply.productdashboard.entity.Airline;
import com.bsupply.productdashboard.exception.AirlineNotFoundException;
import com.bsupply.productdashboard.exception.DuplicateAirlineNameException;
import com.bsupply.productdashboard.factory.AirlineResponseFactory;
import com.bsupply.productdashboard.repository.AirlineRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AirlineService {

    private final AirlineRepository airlineRepository;

    public AirlineService(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    @CacheEvict(
            value = {"airlines", "airlineById"},
            allEntries = true)
    public void addAirline(AirlineRequest airlineRequest) {

        checkIfNameExists(airlineRequest.name());
        log.info("Create new airline with {}", airlineRequest);
        Airline airline = new Airline();
        airline.setName(airlineRequest.name());
        airline.setDescription(airlineRequest.description());
        airlineRepository.save(airline);
    }

    @Cacheable(value = "airlines")
    public List<AirlineResponse> getAirlines() {

        log.info("Fetch all airlines");
        return airlineRepository.findAll()
                .stream()
                .map(a -> AirlineResponseFactory.getAirlineResponse(a))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "airlineById")
    public AirlineResponse getAirlineById(UUID airlineId) {

        log.info("Fetch airline with id: {}", airlineId);
        return airlineRepository
                .findById(airlineId)
                .map(a -> new AirlineResponse(a.getId(), a.getName(), a.getDescription()))
                .orElseThrow(() -> new AirlineNotFoundException(airlineId.toString()));
    }

    private void checkIfNameExists(String airlineName) {
        if (airlineRepository.existsByName(airlineName)) {
            throw new DuplicateAirlineNameException(airlineName);
        }
    }

    @CacheEvict(
            value = {"airlines", "airlineById"},
            allEntries = true)
    public AirlineResponse updateAirline(UUID airlineId, AirlineRequest airlineRequest) {
        log.info("Update airline with id: {}", airlineId);

        Airline airline = airlineRepository.findById(airlineId)
                .orElseThrow(() -> new AirlineNotFoundException(airlineId.toString()));

        airline.setName(airlineRequest.name());
        airline.setDescription(airlineRequest.description());

        Airline updatedAirline = airlineRepository.save(airline);

        return AirlineResponseFactory.getAirlineResponse(updatedAirline);
    }

    @CacheEvict(
            value = {"airlines", "airlineById"},
            allEntries = true)
    public void deleteAirline(UUID airlineId) {

        log.info("Attempting to delete airline with id: {}", airlineId);
        airlineRepository.deleteById(airlineId);
        log.info("Deleted airline with id: {}", airlineId);
    }
}
