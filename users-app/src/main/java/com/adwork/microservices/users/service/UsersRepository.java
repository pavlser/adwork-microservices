package com.adwork.microservices.users.service;

import com.adwork.microservices.users.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface UsersRepository extends JpaRepository<UserAccount, Long> {

    UserAccount findByEmail(String email);

    @Query("select count(*) from UserAccount")
    public Long usersCount();

}
