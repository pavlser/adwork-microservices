package com.adwork.microservices.users.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.adwork.microservices.users.dto.UserDto;
import com.adwork.microservices.users.entity.UserAccount;
import com.adwork.microservices.users.service.IUserService;

@RestController
@RequestMapping("/api/users")
public class UsersController {

	IUserService service;

	public UsersController(IUserService service) {
		this.service = service;
	}

	@GetMapping("{id}")
	//@PreAuthorize("#username == authentication.principal.username") public String getMyRoles(String username) {
	public UserDto one(@PathVariable("id") Long userId) {
		return toDto(service.getUser(userId));
	}

	@RequestMapping(path = "all", method = RequestMethod.GET)
	@PreAuthorize("hasPermission(principal, 'ADMIN') or hasPermission(principal, 'MANAGER')")
	public List<UserDto> all(Principal principal) {
		return service.listUsers().stream()
			.map(userAccount -> toDto(userAccount))
			.collect(Collectors.toList());
	}

	@PostMapping
	@PreAuthorize("hasPermission(principal, 'ADMIN')")
	public UserDto add(@RequestBody UserDto user) {
		UserAccount account = new UserAccount();
		account.setEmail(user.email);
		account.setRole(user.role);
		account.setLocked(user.locked);
		return toDto(service.addUser(account));
	}

	@DeleteMapping("{id}")
	@PreAuthorize("hasPermission(principal, 'ADMIN')")
	public boolean delete(@PathVariable("id") Long userId) {
		return service.deleteUser(userId) != null;
	}

	@PutMapping
	@PreAuthorize("hasPermission(principal, 'ADMIN') or hasPermission(principal, 'MANAGER')")
	public UserDto update(@RequestBody UserDto user) {
		UserAccount account = service.getUser(user.id);
		account.setEmail(user.email);
		account.setRole(user.role);
		account.setLocked(user.locked);
		return toDto(service.updateUser(account));
	}

	private UserDto toDto(UserAccount acc) {
		return new UserDto(acc.getId(), acc.getEmail(), acc.getRole(), acc.isLocked());
	}

}
