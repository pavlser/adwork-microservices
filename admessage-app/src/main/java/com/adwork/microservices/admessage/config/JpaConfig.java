package com.adwork.microservices.admessage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@EnableJpaAuditing//(auditorAwareRef = "auditorAware")
public class JpaConfig {
	
	/*@Bean
	public AuditorAware<String> auditorAware() {
		return new AuditorAwareImpl();
	}*/
	
}
