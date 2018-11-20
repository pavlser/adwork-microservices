package com.adwork.microservices.users.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.adwork.microservices.users.auth.UsersAuthenticationProvider;
import com.adwork.microservices.users.jwt.JwtTokenFilterConfigurer;
import com.adwork.microservices.users.jwt.JwtTokenProvider;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/*
	 * @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	 * 
	 * @Override public AuthenticationManager authenticationManagerBean() throws
	 * Exception { return super.authenticationManagerBean(); }
	 */

	@Autowired
	UsersAuthenticationProvider authProvider;

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // don't need sessions with JWT
		http.csrf().disable(); // no sessions -> no cookies forgery
		http.authorizeRequests().antMatchers("/api/auth/**").permitAll().anyRequest().authenticated();
		http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

}
