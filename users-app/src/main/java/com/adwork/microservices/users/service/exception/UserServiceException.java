package com.adwork.microservices.users.service.exception;

import org.springframework.http.HttpStatus;

public class UserServiceException extends RuntimeException {

    private static final long serialVersionUID = 123L;

    private final String message;
    private final HttpStatus httpStatus;

    public UserServiceException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
