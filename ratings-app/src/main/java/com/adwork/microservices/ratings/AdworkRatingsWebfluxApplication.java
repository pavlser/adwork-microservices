package com.adwork.microservices.ratings;

import static org.springframework.web.reactive.function.BodyExtractors.toMono;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.adwork.microservices.ratings.functional.FeaturesEndpoint;
import com.adwork.microservices.ratings.functional.RatingsEntity;
import com.adwork.microservices.ratings.functional.RatingsReactiveRepository;

@SpringBootApplication
@EnableWebFluxSecurity
@EnableReactiveMongoRepositories
public class AdworkRatingsWebfluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdworkRatingsWebfluxApplication.class, args);
	}
	
	@Bean
	public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
		return http
				.authorizeExchange().matchers(EndpointRequest.to(FeaturesEndpoint.class)).permitAll()
				.anyExchange().permitAll()
				.and().csrf().disable()
				.build();
	}
	
	@Autowired
	RatingsReactiveRepository repository;
	
	// get rating/ad/{id}
	// post rating/ad/1?comment=1&value=-1|0|1
	
	@Bean
	RouterFunction<ServerResponse> getMyRatingsRoute() {
		return route(GET("/api/ratings"), req -> ok().body(repository.findAll(), RatingsEntity.class));
	}

	/*@Bean
	RouterFunction<ServerResponse> getEmployeeByIdRoute() {
		return route(GET("/employees/{userId}"),
				req -> ok().body(repository.findByUser(req.pathVariable("userId")), RatingsEntity.class));
	}*/

	@Bean
	RouterFunction<ServerResponse> updatetMyRatingsRoute() {
	/*	return route(POST("/api/ratings"), req -> 
			req.body(toMono(RatingsEntity.class))
				.doOnNext(repository::updateRating).then(ok().build())); */
		return null;
	}

	/*@Bean
	RouterFunction<ServerResponse> composedRoutes() {
		return route(GET("/employees"), req -> ok().body(repository.findAll(), RatingsEntity.class))
				.and(route(GET("/employees/{id}"),
						req -> ok().body(repository.findById(req.pathVariable("id")), RatingsEntity.class)))
				.and(route(POST("/employees/update"), req -> req.body(toMono(RatingsEntity.class))
						.doOnNext(repository::updateRating).then(ok().build())));
	}*/

}

class RatingDto {
	Long ad;
	Long comment;
	Integer value;
	Date date;
}

