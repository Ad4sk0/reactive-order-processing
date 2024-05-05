package com.example.inventory.repository;

import com.example.inventory.entity.ProductOrderCancellationEntity;
import io.micronaut.data.mongodb.annotation.MongoFindQuery;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;

@MongoRepository
public interface ProductCancellationRepository
    extends ReactorCrudRepository<@Valid ProductOrderCancellationEntity, ObjectId> {

  @MongoFindQuery(filter = "{productOrderId: {$in: :productOrderIds}}")
  Flux<ProductOrderCancellationEntity> findByproductOrderIds(List<ObjectId> productOrderIds);
}
