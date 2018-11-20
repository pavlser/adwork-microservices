package com.adwork.microservices.users.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.adwork.microservices.users.entity.UserAccount;
import com.adwork.microservices.users.jwt.JwtTokenProvider;
import com.adwork.microservices.users.service.exception.UserNotFoundException;
import com.adwork.microservices.users.service.exception.UserServiceException;

@Service
public class UserService implements IUserService {

	@Autowired
	private UsersRepository repository;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	public List<UserAccount> listUsers() {
		return repository.findAll();
	}

	@Override
	public UserAccount getUser(Long id) {
		UserAccount user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		return user;
	}

	@Override
	public UserAccount addUser(UserAccount user) {
		return repository.save(user);
	}

	@Override
	public UserAccount updateUser(UserAccount user) {
		UserAccount updatedUser = repository.findById(user.getId()).map(account -> {
			account.setEmail(user.getEmail());
			account.setName(user.getName());
			account.setLocked(user.isLocked());
			account.setUpdatedDate(new Date());
			return repository.save(account);
		}).orElseThrow(() -> new UserNotFoundException(user.getId()));
		return updatedUser;
	}

	@Override
	@Transactional
	public UserAccount deleteUser(Long id) {
		UserAccount user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		repository.delete(user);
		return null;
	}

	@Override
	public Long countUsers() {
		return repository.usersCount();
	}

	@Transactional 
	public String authenticateByEmail(String email, String referer) {
		UserAccount user = repository.findByEmail(email);
		if (user != null) {
			return jwtTokenProvider.createToken(user.getEmail(), user.getRoles(), referer);
		} else {
			throw new UserServiceException("Invalid username or password", HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	/*
	 * @Transactional public String register(UserAccount user) { if
	 * (repository.findByEmail(user.getEmail()) == null) {
	 * user.setPassword(passwordEncoder.encode(user.getPassword()));
	 * repository.save(user); return jwtTokenProvider.createToken(user.getEmail(),
	 * user.getRoles()); } else { throw new UserServiceException("User " +
	 * user.getEmail() + " already exists", HttpStatus.UNPROCESSABLE_ENTITY); } }
	 */
}
