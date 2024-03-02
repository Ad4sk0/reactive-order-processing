package com.example.inventory.repository;

import com.example.inventory.entity.ProductEntity;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@MongoRepository
public interface ProductRepository extends ReactorCrudRepository<@Valid ProductEntity, ObjectId> {

  Mono<ProductEntity> findByName(String name);

  Flux<ProductEntity> findByIds(List<ObjectId> objectIds);
}
