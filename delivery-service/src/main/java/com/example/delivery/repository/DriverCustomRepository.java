package com.example.delivery.repository;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import com.example.delivery.entity.DriverEntity;
import com.example.delivery.entity.DriverStatus;
import infrastructure.repository.CustomMongoRepository;
import infrastructure.repository.provider.MongoProviders;
import jakarta.inject.Singleton;
import org.bson.conversions.Bson;
import reactor.core.publisher.Mono;

@Singleton
public class DriverCustomRepository {

  private final CustomMongoRepository<DriverEntity> customMongoRepository;

  public DriverCustomRepository(MongoProviders mongoProviders) {
    this.customMongoRepository = new CustomMongoRepository<>(DriverEntity.class, mongoProviders);
  }

  public Mono<DriverEntity> findFirstFreeDriverAndChangeStatus(DriverStatus status) {
    Bson filter = eq("status", DriverStatus.FREE);
    Bson update = set("status", status);
    return customMongoRepository.findAndUpdate(filter, update);
  }
}
