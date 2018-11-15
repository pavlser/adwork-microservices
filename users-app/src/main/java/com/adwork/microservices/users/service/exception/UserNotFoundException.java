package com.adwork.microservices.users.service.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("User with id '" + id + "' is not found");
    }
}
