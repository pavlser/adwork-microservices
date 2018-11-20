package com.adwork.microservices.users.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adwork.microservices.users.entity.UserAccount;
import com.adwork.microservices.users.service.exception.UserNotFoundException;

@Service
public class UserService implements IUserService {

	@Autowired
	private UsersRepository repository;

	@Override
	public List<UserAccount> listUsers() {
		return repository.findAll();
	}

	@Override
	public UserAccount getUser(Long id) {
		UserAccount user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		return user;
	}
	
	public UserAccount getUserByEmailAndPassword(String email, String password) {
		UserAccount user = repository.findByEmail(email);
		return user.getPassword().equals(password) ? user : null;
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


	/*
	 * @Transactional public String register(UserAccount user) { if
	 * (repository.findByEmail(user.getEmail()) == null) {
	 * user.setPassword(passwordEncoder.encode(user.getPassword()));
	 * repository.save(user); return jwtTokenProvider.createToken(user.getEmail(),
	 * user.getRoles()); } else { throw new UserServiceException("User " +
	 * user.getEmail() + " already exists", HttpStatus.UNPROCESSABLE_ENTITY); } }
	 */
}
