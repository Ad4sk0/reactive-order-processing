package com.example.delivery.service;

import com.example.delivery.entity.DriverEntity;
import com.example.delivery.repository.DriverRepository;
import com.example.models.DeliveryInfo;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import jakarta.inject.Singleton;

@Singleton
public class DriverServiceImpl implements DriverService {

  private final DriverRepository driverRepository;

  public DriverServiceImpl(DriverRepository driverRepository) {
    this.driverRepository = driverRepository;
  }

  @Override
  public Maybe<DriverEntity> findAvailableDriver(DeliveryInfo deliveryInfo) {
    return findFirstFreeDriver();
  }

  private Maybe<DriverEntity> findFirstFreeDriver() {
    return Flowable.fromPublisher(driverRepository.findFreeDrivers()).firstElement();
  }
}
