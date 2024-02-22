package com.example.delivery.service;

import com.example.delivery.entity.DriverEntity;
import com.example.delivery.entity.DriverStatus;
import com.example.models.DeliveryInfo;
import reactor.core.publisher.Mono;

public interface DriverService {
  Mono<DriverEntity> findAvailableDriver(DeliveryInfo deliveryInfo);

  Mono<DriverEntity> findFirstFreeDriverAndChangeStatus(DriverStatus status);
}
