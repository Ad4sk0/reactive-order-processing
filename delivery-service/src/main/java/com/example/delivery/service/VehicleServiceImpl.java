package com.example.delivery.service;

import com.example.delivery.entity.VehicleEntity;
import com.example.delivery.repository.VehicleRepository;
import com.example.models.DeliveryInfo;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import jakarta.inject.Singleton;

@Singleton
public class VehicleServiceImpl implements VehicleService {

  private final VehicleRepository vehicleRepository;

  public VehicleServiceImpl(VehicleRepository vehicleRepository) {
    this.vehicleRepository = vehicleRepository;
  }

  @Override
  public Maybe<VehicleEntity> findAvailableVehicle(DeliveryInfo deliveryInfo) {
    return findFirstFreeVehicle();
  }

  private Maybe<VehicleEntity> findFirstFreeVehicle() {
    return Flowable.fromPublisher(vehicleRepository.findFreeVehicles()).firstElement();
  }
}
