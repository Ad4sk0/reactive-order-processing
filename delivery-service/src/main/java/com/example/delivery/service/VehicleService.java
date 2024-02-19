package com.example.delivery.service;

import com.example.delivery.entity.VehicleEntity;
import com.example.models.DeliveryInfo;
import reactor.core.publisher.Mono;

public interface VehicleService {
  Mono<VehicleEntity> findAvailableVehicle(DeliveryInfo deliveryInfo);
}
