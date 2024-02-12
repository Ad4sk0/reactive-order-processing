package com.example.delivery.repository;

import com.example.delivery.entity.DeliveryEntity;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;

@MongoRepository
public interface DeliveryRepository
    extends ReactiveStreamsCrudRepository<@Valid DeliveryEntity, @Valid ObjectId> {}
