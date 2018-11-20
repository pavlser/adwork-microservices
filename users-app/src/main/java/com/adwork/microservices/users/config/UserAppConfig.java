package com.adwork.microservices.users.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.adwork.microservices.users.entity.UserAccount;
import com.adwork.microservices.users.entity.UserRole;
import com.adwork.microservices.users.service.IUserService;

@Configuration
public class UserAppConfig {

	@Bean
	CommandLineRunner initDatabase(IUserService service) {
		return args -> {
			if (service.countUsers() == 0) {
				service.addUser(new UserAccount("admin@adwork-microservices.com", "admin", UserRole.ADMIN));
				service.addUser(new UserAccount("user1@adwork-microservices.com", "user1", UserRole.USER));
				service.addUser(new UserAccount("manager1@adwork-microservices.com", "manager1", UserRole.MANAGER));
				System.out.println("Users created:\n" + service.listUsers());
			}
		};
	}

}
