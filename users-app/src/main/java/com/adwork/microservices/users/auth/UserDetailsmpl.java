package com.adwork.microservices.users.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.adwork.microservices.users.entity.UserAccount;

public class UserDetailsmpl implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	private UserAccount user;
	
	public UserDetailsmpl(UserAccount user) {
		this.user = user;
	}
	
	public UserDetailsmpl(String email, String password) {
		this.user = new UserAccount();
		this.user.setEmail(email);
		this.user.setPassword(password);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getAuthorities();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; //TODO: isAccountNonExpired
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; //TODO: isAccountNonLocked
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; //TODO: isCredentialsNonExpired
	}

	@Override
	public boolean isEnabled() {
		return true; //TODO: isEnabled
	}

}
