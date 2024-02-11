package com.example.inventory.repository;

import com.example.inventory.entity.ProductEntity;
import io.micronaut.data.mongodb.annotation.MongoFindQuery;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.reactivestreams.Publisher;

@MongoRepository
public interface ProductRepository
    extends ReactiveStreamsCrudRepository<@Valid ProductEntity, ObjectId> {

  @MongoFindQuery(filter = "{name:{$regex: :name}}")
  Publisher<ProductEntity> findByName(String name);
}
