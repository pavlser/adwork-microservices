package com.adwork.microservices.admessage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.adwork.microservices.admessage")
@EnableJpaAuditing
public class JpaConfig {
	
}
