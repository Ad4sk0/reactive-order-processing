package com.example.delivery.repository;

import com.example.delivery.entity.VehicleEntity;
import com.example.delivery.entity.VehicleStatus;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.mongodb.annotation.MongoFindQuery;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@MongoRepository
public interface VehicleRepository extends ReactorCrudRepository<@Valid VehicleEntity, ObjectId> {

  @MongoFindQuery(filter = "{status: 'FREE'}")
  Flux<VehicleEntity> findFreeVehicles();

  Mono<Integer> updateStatus(@Id ObjectId objectId, VehicleStatus status);
}
