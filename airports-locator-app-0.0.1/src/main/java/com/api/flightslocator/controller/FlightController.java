package com.api.flightslocator.controller;

import com.api.flightslocator.exception.FlightNotFoundException;
import com.api.flightslocator.model.Flights;
import com.api.flightslocator.repository.FlightRepository;
import com.api.flightslocator.constants.FlightLocatorConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sysapi")
public class FlightController {

    @Autowired
    FlightRepository flightRepository;
    Flights flight;

    // Get All Flights
    @GetMapping("/v1/airport")
    public List<Flights> getAllFlights() {
        return flightRepository.findAll(orderByIdAsc());
    }
    
    private Sort orderByIdAsc() {
    	return new Sort(Sort.Direction.ASC, "airportId");
    }

    // Get a Single Flight
    /* Return type of Flights entity could have been used instead of List<Flights> as the method 
    always returns a single flight. But the return type of List<Flights> has made it easy for
    validating isEmpty() after the JPA query */
    @GetMapping("/v1/airport/{id}")
    public List<Flights> getFlightById(@PathVariable(value = "id") String IATA){
        System.out.println("Enetered IATA Method: " + IATA);
        List<Flights> flights = null;
        flights = flightRepository.findByIATA(IATA);
        if(flights.isEmpty()) {
        	System.out.println("FlightNotFoundException IATA!");
        	throw new FlightNotFoundException(FlightLocatorConstants.FlightNotFoundExceptionIATAMessage + IATA);     
        } 
        System.out.println("IATA Found");
        return flights;
    }
    
    // Get Flights By City
    @GetMapping("/v1/airport/city/{id}")
    public List<Flights> getFlightByCity(@PathVariable(value = "id") String city){
        System.out.println("City: " + city);
        List<Flights> flights = null;
        flights = flightRepository.findByCity(city, orderByIdAsc());  
        if(flights.isEmpty()) {
    		System.out.println("FlightNotFoundException city!");
    		throw new FlightNotFoundException(FlightLocatorConstants.FlightNotFoundExceptionCityMessage + city);
    	}
        System.out.println("City Found");
        return flights;
        
    }

    // Delete a Flight
    /* Return type of Flights entity could have been used instead of List<Flights> as the method 
    always returns a single flight. But the return type of List<Flights> has made it easy for
    validating isEmpty() after the JPA query */
    @DeleteMapping("/v1/airport/{id}")
    public ResponseEntity<?> deleteFlight(@PathVariable(value = "id") String IATA) {
    	List<Flights> flights = null;
        flights = flightRepository.findByIATA(IATA);
        if(flights.isEmpty()) {
        	System.out.println("FlightNotFoundException IATA!");
        	throw new FlightNotFoundException(FlightLocatorConstants.FlightNotFoundExceptionIATAMessage + IATA);     
        } 
        for (int i = 0; i < flights.size(); i++) {
        	flight = flights.get(i);
        	flightRepository.delete(flight);  
		}        
        return ResponseEntity.ok().build();
    }
   
}