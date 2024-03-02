package com.example.inventory.repository;

import com.example.inventory.entity.ProductOrderEntity;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;

@MongoRepository
public interface ProductOrderRepository
    extends ReactorCrudRepository<@Valid ProductOrderEntity, ObjectId> {}
