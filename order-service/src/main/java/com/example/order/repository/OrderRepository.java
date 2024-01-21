package com.example.order.repository;

import com.example.order.entity.OrderEntity;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;

@MongoRepository
public interface OrderRepository extends ReactiveStreamsCrudRepository<OrderEntity, String> {}
