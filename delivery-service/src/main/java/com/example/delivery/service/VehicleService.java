package com.example.delivery.service;

import com.example.delivery.entity.VehicleEntity;
import com.example.delivery.entity.VehicleStatus;
import com.example.models.DeliveryInfo;
import org.bson.types.ObjectId;
import reactor.core.publisher.Mono;

public interface VehicleService {
  Mono<VehicleEntity> findAvailableVehicle(DeliveryInfo deliveryInfo);

  Mono<VehicleEntity> findFirstFreeVehicleAndChangeStatus(VehicleStatus status);

  Mono<Integer> updateVehicleStatus(ObjectId objectId, VehicleStatus status);
}
