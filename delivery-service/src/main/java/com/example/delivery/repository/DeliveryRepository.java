package com.example.delivery.repository;

import com.example.delivery.entity.DeliveryEntity;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;

@MongoRepository
public interface DeliveryRepository
    extends ReactorCrudRepository<@Valid DeliveryEntity, @Valid ObjectId> {}
