package com.example.delivery.repository;

import com.example.delivery.entity.DriverEntity;
import io.micronaut.data.mongodb.annotation.MongoFindQuery;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.reactivestreams.Publisher;

@MongoRepository
public interface DriverRepository
    extends ReactiveStreamsCrudRepository<@Valid DriverEntity, @Valid ObjectId> {

  @MongoFindQuery(filter = "{status: 'FREE'}")
  Publisher<DriverEntity> findFreeDrivers();
}
