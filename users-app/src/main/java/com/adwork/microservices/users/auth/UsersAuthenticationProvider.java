package com.adwork.microservices.users.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.adwork.microservices.users.entity.UserAccount;
import com.adwork.microservices.users.service.UserService;

@Component
public class UsersAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	UserService usersService;
	
	public Authentication authenticate(String email, String password) throws AuthenticationException {
		return authenticate(new UsernamePasswordAuthenticationToken(email, password));
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String email = authentication.getName();
		String password = authentication.getCredentials().toString();
		UserAccount user = usersService.getUserByEmailAndPassword(email, password);
		return user != null ? new UsernamePasswordAuthenticationToken(email, password, user.getAuthorities()) : null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
