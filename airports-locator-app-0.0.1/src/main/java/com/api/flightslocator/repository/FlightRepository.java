package com.api.flightslocator.repository;


import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.flightslocator.model.Flights;

@Repository
public interface FlightRepository extends JpaRepository<Flights, Long> {

	@Query("select f from Flights f where f.IATA = :IATA")
	List<Flights> findByIATA(@Param("IATA") String IATA);
	
	@Query("select f from Flights f where f.city = :city")
	List<Flights> findByCity(@Param("city") String city, Sort orderByIdAsc);
	
}