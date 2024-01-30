package com.example.inventory.repository;

import com.example.inventory.entity.ProductOrderEntity;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import jakarta.validation.Valid;

@MongoRepository
public interface ProductOrderRepository
    extends ReactiveStreamsCrudRepository<@Valid ProductOrderEntity, String> {}
