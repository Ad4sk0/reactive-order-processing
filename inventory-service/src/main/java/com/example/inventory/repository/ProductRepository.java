package com.example.inventory.repository;

import com.example.inventory.entity.ProductEntity;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;

@MongoRepository
public interface ProductRepository
    extends ReactiveStreamsCrudRepository<@Valid ProductEntity, ObjectId> {}
