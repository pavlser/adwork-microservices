package com.adwork.microservices.admessage.conrtoller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class ExceptionController extends ResponseEntityExceptionHandler {

	@ExceptionHandler(AdMessagesException.class)
	public final ResponseEntity<String> handleAdMessagesException(AdMessagesException ex, WebRequest request) {
	  return getResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<String> handleAllExceptions(Exception ex, WebRequest request) {
	  String error = "Error: " + ex.getMessage() + "\n" + request.getDescription(false);
	  return getResponse(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	ResponseEntity<String> getResponse(String message, HttpStatus code) {
		return new ResponseEntity<>(message, code);
	}
	
}
