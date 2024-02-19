package com.example.delivery.repository;

import com.example.delivery.entity.VehicleEntity;
import io.micronaut.data.mongodb.annotation.MongoFindQuery;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;

@MongoRepository
public interface VehicleRepository
    extends ReactorCrudRepository<@Valid VehicleEntity, @Valid ObjectId> {

  @MongoFindQuery(filter = "{status: 'FREE'}")
  Flux<VehicleEntity> findFreeVehicles();
}
