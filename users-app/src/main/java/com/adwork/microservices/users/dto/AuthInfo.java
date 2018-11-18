package com.adwork.microservices.users.dto;

public class AuthInfo {
	public String email;
	public String password;
	
	public AuthInfo() {}
	
	public AuthInfo(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
