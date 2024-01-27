package com.example.order.repository;

import com.example.order.entity.OrderEntity;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import jakarta.validation.Valid;

@MongoRepository
public interface OrderRepository
    extends ReactiveStreamsCrudRepository<@Valid OrderEntity, @Valid String> {}
