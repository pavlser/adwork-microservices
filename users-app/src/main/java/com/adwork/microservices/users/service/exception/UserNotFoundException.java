package com.adwork.microservices.users.service.exception;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserNotFoundException(Long id) {
        super("User with id '" + id + "' is not found");
    }
}
