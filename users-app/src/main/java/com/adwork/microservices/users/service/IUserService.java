package com.adwork.microservices.users.service;

import com.adwork.microservices.users.entity.UserAccount;

import java.util.List;

public interface IUserService {

    List<UserAccount> listUsers();

    UserAccount getUser(Long id);

    UserAccount addUser(UserAccount user);

    UserAccount updateUser(UserAccount user);

    UserAccount deleteUser(Long id);

    Long countUsers();

}
