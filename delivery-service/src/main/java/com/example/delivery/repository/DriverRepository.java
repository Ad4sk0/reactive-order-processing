package com.example.delivery.repository;

import com.example.delivery.entity.DriverEntity;
import com.example.delivery.entity.DriverStatus;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.mongodb.annotation.MongoFindQuery;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@MongoRepository
public interface DriverRepository extends ReactorCrudRepository<@Valid DriverEntity, ObjectId> {

  @MongoFindQuery(filter = "{status: 'FREE'}")
  Flux<DriverEntity> findFreeDrivers();

  Mono<Integer> updateStatus(@Id ObjectId id, DriverStatus status);
}
