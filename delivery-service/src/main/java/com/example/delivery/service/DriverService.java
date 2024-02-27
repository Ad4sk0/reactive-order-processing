package com.example.delivery.service;

import com.example.delivery.entity.DriverEntity;
import com.example.delivery.entity.DriverStatus;
import com.example.models.DeliveryInfo;
import org.bson.types.ObjectId;
import reactor.core.publisher.Mono;

public interface DriverService {
  Mono<DriverEntity> findAvailableDriver(DeliveryInfo deliveryInfo);

  Mono<DriverEntity> findFirstFreeDriverAndChangeStatus(DriverStatus status);

  Mono<Integer> updateDriverStatus(ObjectId objectId, DriverStatus status);
}
