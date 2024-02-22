package com.example.delivery.service;

import com.example.delivery.entity.VehicleEntity;
import com.example.delivery.entity.VehicleStatus;
import com.example.delivery.repository.VehicleCustomRepository;
import com.example.delivery.repository.VehicleRepository;
import com.example.models.DeliveryInfo;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Singleton
public class VehicleServiceImpl implements VehicleService {

  private final VehicleRepository vehicleRepository;
  private final VehicleCustomRepository vehicleCustomRepository;

  public VehicleServiceImpl(
      VehicleRepository vehicleRepository, VehicleCustomRepository vehicleCustomRepository) {
    this.vehicleRepository = vehicleRepository;
    this.vehicleCustomRepository = vehicleCustomRepository;
  }

  @Override
  public Mono<VehicleEntity> findAvailableVehicle(DeliveryInfo deliveryInfo) {
    return findFirstFreeVehicle();
  }

  @Override
  public Mono<VehicleEntity> findFirstFreeVehicleAndChangeStatus(VehicleStatus status) {
    return vehicleCustomRepository.findFirstFreeVehicleAndChangeStatus(status);
  }

  private Mono<VehicleEntity> findFirstFreeVehicle() {
    return vehicleRepository.findFreeVehicles().next();
  }
}
