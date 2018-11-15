package com.adwork.microservices.users.service;

import com.adwork.microservices.users.config.JwtTokenProvider;
import com.adwork.microservices.users.entity.UserAccount;
import com.adwork.microservices.users.service.exception.UserNotFoundException;
import com.adwork.microservices.users.service.exception.UserServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UsersRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public List<UserAccount> listUsers() {
        return repository.findAll();
    }

    @Override
    public UserAccount getUser(Long id) {
        UserAccount user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return user;
    }

    @Override
    public UserAccount addUser(UserAccount user) {
        return repository.save(user);
    }

    @Override
    public UserAccount updateUser(UserAccount user) {
        UserAccount updatedUser = repository.findById(user.getId())
                .map(account -> {
                    account.setEmail(user.getEmail());
                    account.setName(user.getName());
                    account.setLocked(user.isLocked());
                    account.setUpdatedDate(new Date());
                    return repository.save(account);
                }).orElseThrow(() ->
                        new UserNotFoundException(user.getId()));
        return updatedUser;
    }

    @Override
    @Transactional
    public UserAccount deleteUser(Long id) {
        UserAccount user = repository.findById(id).orElseThrow(() ->
                new UserNotFoundException(id));
        repository.delete(user);
        return null;
    }

    @Override
    public Long countUsers() {
        return repository.usersCount();
    }

    public String signin(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            return jwtTokenProvider.createToken(email, repository.findByEmail(email).getRoles());
        } catch (AuthenticationException e) {
            throw new UserServiceException("Invalid username or password", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Transactional
    public String register(UserAccount user) {
        if (repository.findByEmail(user.getEmail()) == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            repository.save(user);
            return jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
        } else {
            throw new UserServiceException("User " + user.getEmail() + " already exists", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
