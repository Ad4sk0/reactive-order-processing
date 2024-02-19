package com.example.delivery.repository;

import com.example.delivery.entity.DriverEntity;
import io.micronaut.data.mongodb.annotation.MongoFindQuery;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;

@MongoRepository
public interface DriverRepository
    extends ReactorCrudRepository<@Valid DriverEntity, @Valid ObjectId> {

  @MongoFindQuery(filter = "{status: 'FREE'}")
  Flux<DriverEntity> findFreeDrivers();
}
