package com.adwork.microservices.admessage.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adwork.microservices.admessage.entity.AdmessageEntity;

public interface AdmessagesRepository extends JpaRepository<AdmessageEntity, Long> {

}
