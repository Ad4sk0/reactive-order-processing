package com.example.delivery.service;

import com.example.delivery.entity.VehicleEntity;
import com.example.delivery.repository.VehicleRepository;
import com.example.models.DeliveryInfo;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Singleton
public class VehicleServiceImpl implements VehicleService {

  private final VehicleRepository vehicleRepository;

  public VehicleServiceImpl(VehicleRepository vehicleRepository) {
    this.vehicleRepository = vehicleRepository;
  }

  @Override
  public Mono<VehicleEntity> findAvailableVehicle(DeliveryInfo deliveryInfo) {
    return findFirstFreeVehicle();
  }

  private Mono<VehicleEntity> findFirstFreeVehicle() {
    return vehicleRepository.findFreeVehicles().next();
  }
}
