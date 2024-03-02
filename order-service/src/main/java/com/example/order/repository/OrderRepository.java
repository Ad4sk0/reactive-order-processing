package com.example.order.repository;

import com.example.order.entity.OrderEntity;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;

@MongoRepository
public interface OrderRepository
    extends ReactiveStreamsCrudRepository<@Valid OrderEntity, ObjectId> {}
