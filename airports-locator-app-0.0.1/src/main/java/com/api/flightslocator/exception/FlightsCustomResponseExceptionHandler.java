package com.api.flightslocator.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.api.flightslocator.exception.FlightNotFoundException;

@ControllerAdvice
@RestController
public class FlightsCustomResponseExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(FlightNotFoundException.class)
  public final ResponseEntity<FlightError> handleUserNotFoundException(FlightNotFoundException ex, WebRequest request) {
	  FlightError flightError = new FlightError(new Date(), ex.getMessage(), request.getDescription(false));
	  return new ResponseEntity<>(flightError, HttpStatus.NOT_FOUND);
  }
  
  @ExceptionHandler(Exception.class)
  public final ResponseEntity<FlightError> handleAllExceptions(Exception ex, WebRequest request) {
	  FlightError flightError = new FlightError(new Date(), ex.getMessage(), request.getDescription(false));
	  return new ResponseEntity<>(flightError, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
