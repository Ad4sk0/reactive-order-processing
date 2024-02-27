package com.example.delivery.service;

import com.example.delivery.entity.DriverEntity;
import com.example.delivery.entity.DriverStatus;
import com.example.delivery.repository.DriverCustomRepository;
import com.example.delivery.repository.DriverRepository;
import com.example.models.DeliveryInfo;
import jakarta.inject.Singleton;
import org.bson.types.ObjectId;
import reactor.core.publisher.Mono;

@Singleton
public class DriverServiceImpl implements DriverService {

  private final DriverRepository driverRepository;
  private final DriverCustomRepository driverCustomRepository;

  public DriverServiceImpl(
      DriverRepository driverRepository, DriverCustomRepository driverCustomRepository) {
    this.driverRepository = driverRepository;
    this.driverCustomRepository = driverCustomRepository;
  }

  @Override
  public Mono<DriverEntity> findAvailableDriver(DeliveryInfo deliveryInfo) {
    return findFirstFreeDriver();
  }

  @Override
  public Mono<DriverEntity> findFirstFreeDriverAndChangeStatus(DriverStatus status) {
    return driverCustomRepository.findFirstFreeDriverAndChangeStatus(status);
  }

  @Override
  public Mono<Integer> updateDriverStatus(ObjectId objectId, DriverStatus status) {
    return driverRepository.updateStatus(objectId, status);
  }

  private Mono<DriverEntity> findFirstFreeDriver() {
    return driverRepository.findFreeDrivers().next();
  }
}
