package com.example.inventory.repository;

import com.example.inventory.entity.ProductOrderEntity;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;

@MongoRepository
public interface ProductOrderRepository
    extends ReactorCrudRepository<@Valid ProductOrderEntity, ObjectId> {

  Flux<ProductOrderEntity> findByIds(List<ObjectId> objectIds);
}
