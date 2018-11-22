package com.adwork.microservices.admessage.conrtoller;

import org.springframework.http.HttpStatus;

public class AdMessagesException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public static final AdMessagesException AdNotFound = new AdMessagesException("Ad not found", HttpStatus.NOT_ACCEPTABLE);
	
	private HttpStatus httpCode;
	
	public AdMessagesException(String message, HttpStatus httpCode) {
		super(message);
		this.httpCode = httpCode;
	}
	
	public HttpStatus getCode() {
		return httpCode;
	}

}
