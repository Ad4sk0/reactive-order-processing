package com.example.delivery.service;

import com.example.delivery.entity.DriverEntity;
import com.example.delivery.repository.DriverRepository;
import com.example.models.DeliveryInfo;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Singleton
public class DriverServiceImpl implements DriverService {

  private final DriverRepository driverRepository;

  public DriverServiceImpl(DriverRepository driverRepository) {
    this.driverRepository = driverRepository;
  }

  @Override
  public Mono<DriverEntity> findAvailableDriver(DeliveryInfo deliveryInfo) {
    return findFirstFreeDriver();
  }

  private Mono<DriverEntity> findFirstFreeDriver() {
    return driverRepository.findFreeDrivers().next();
  }
}
