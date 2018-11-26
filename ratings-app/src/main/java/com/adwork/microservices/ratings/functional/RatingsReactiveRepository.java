package com.adwork.microservices.ratings.functional;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RatingsReactiveRepository extends ReactiveCrudRepository<RatingsEntity, String> {

    Flux<RatingsEntity> findByUser(String user);

    Mono<RatingsEntity> findById(Long id);
    
    Mono<RatingsEntity> updateRating(Long id);
}
