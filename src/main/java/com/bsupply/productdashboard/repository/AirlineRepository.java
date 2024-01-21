package com.bsupply.productdashboard.repository;

import com.bsupply.productdashboard.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, UUID> {

    boolean existsByName(String name);

}