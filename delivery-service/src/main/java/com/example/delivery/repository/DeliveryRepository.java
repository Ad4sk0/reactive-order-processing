package com.example.delivery.repository;

import com.example.delivery.entity.DeliveryEntity;
import com.example.models.DeliveryStatus;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.mongodb.annotation.MongoUpdateQuery;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import org.bson.types.ObjectId;
import reactor.core.publisher.Mono;

@MongoRepository
public interface DeliveryRepository
    extends ReactorCrudRepository<@Valid DeliveryEntity, @Valid ObjectId> {

  @MongoUpdateQuery(
      filter = "{_id: :objectId}",
      update =
          "{$set:{'deliveryJobStatusEmbeddable.status': :status, 'deliveryJobStatusEmbeddable.estimatedDeliveryTime': :estimatedDeliveryTime}}")
  Mono<Integer> updateStatusAndEstimatedDeliveryTime(
      @NotNull ObjectId objectId,
      @NotNull DeliveryStatus status,
      @Nullable Instant estimatedDeliveryTime);

  @MongoUpdateQuery(
      filter = "{_id: :objectId}",
      update = "{$set:{'deliveryJobStatusEmbeddable.end': :end}}")
  Mono<Integer> updateEndTime(@NotNull ObjectId objectId, Instant end);
}
