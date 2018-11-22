package com.adwork.microservices.admessage.service;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.adwork.microservices.admessage.entity.AdmessageEntity;

public interface AdmessagesRepository extends PagingAndSortingRepository<AdmessageEntity, Long> {

}
