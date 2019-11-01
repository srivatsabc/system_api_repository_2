package com.api.flightslocator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ComponentScan("com.api.flightslocator.controller")
@EnableJpaAuditing
public class FlightsLocatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightsLocatorApplication.class, args);
	}
}
