package com.example.delivery.repository;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import com.example.delivery.entity.VehicleEntity;
import com.example.delivery.entity.VehicleStatus;
import infrastructure.repository.CustomMongoRepository;
import infrastructure.repository.provider.MongoProviders;
import jakarta.inject.Singleton;
import org.bson.conversions.Bson;
import reactor.core.publisher.Mono;

@Singleton
public class VehicleCustomRepository {

  private final CustomMongoRepository<VehicleEntity> customMongoRepository;

  public VehicleCustomRepository(MongoProviders mongoProviders) {
    this.customMongoRepository = new CustomMongoRepository<>(VehicleEntity.class, mongoProviders);
  }

  public Mono<VehicleEntity> findFirstFreeVehicleAndChangeStatus(VehicleStatus status) {
    Bson filter = eq("status", VehicleStatus.FREE);
    Bson update = set("status", status);
    return customMongoRepository.findAndUpdate(filter, update);
  }
}
