package com.adwork.microservices.users.dto;

public class AuthData {
	public String email;
	public String password;
	public String referer;
	
	public AuthData() {}
	
	public AuthData(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public AuthData(String email, String password, String url) {
		this.email = email;
		this.password = password;
		this.referer = url;
	}
}
