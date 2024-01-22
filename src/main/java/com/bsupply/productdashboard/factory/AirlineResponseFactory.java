package com.bsupply.productdashboard.factory;

import com.bsupply.productdashboard.dto.response.AirlineResponse;
import com.bsupply.productdashboard.entity.Airline;

public class AirlineResponseFactory {

     AirlineResponseFactory() {
    }

    public static AirlineResponse getAirlineResponse(Airline airline) {

         return new AirlineResponse(
                 airline.getId(),
                 airline.getName(),
                 airline.getDescription()
         );
    }
}
