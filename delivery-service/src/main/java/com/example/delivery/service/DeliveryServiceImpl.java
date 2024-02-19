package com.example.delivery.service;

import com.example.delivery.entity.DeliveryEntity;
import com.example.delivery.entity.DeliveryJobEmbeddable;
import com.example.delivery.entity.DriverEntity;
import com.example.delivery.entity.VehicleEntity;
import com.example.delivery.mapper.DeliveryMapper;
import com.example.delivery.repository.DeliveryRepository;
import com.example.models.Delivery;
import com.example.models.DeliveryInfo;
import com.example.models.DeliveryStatus;
import jakarta.inject.Singleton;
import jakarta.validation.ValidationException;
import java.time.Instant;
import java.time.LocalDateTime;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Singleton
public class DeliveryServiceImpl implements DeliveryService {

  private final DeliveryRepository deliveryRepository;
  private final AddressInRangeService addressInRangeService;
  private final DriverService driverService;
  private final VehicleService vehicleService;

  public DeliveryServiceImpl(
      DeliveryRepository deliveryRepository,
      AddressInRangeService addressInRangeService,
      DriverService driverService,
      VehicleService vehicleService) {
    this.deliveryRepository = deliveryRepository;
    this.addressInRangeService = addressInRangeService;
    this.driverService = driverService;
    this.vehicleService = vehicleService;
  }

  @Override
  public Flux<Delivery> findAll() {
    return deliveryRepository.findAll().map(DeliveryMapper::toDTO);
  }

  @Override
  public Mono<Delivery> save(Delivery delivery) {
    if (delivery.id() != null) {
      return Mono.error(new UnsupportedOperationException("Id should not be provided"));
    }
    DeliveryInfo deliveryInfo = delivery.deliveryInfo();

    Mono<Boolean> isAddressInRangeMono =
        addressInRangeService
            .isAddressInRange(deliveryInfo)
            .flatMap(
                addressInRange -> {
                  if (!addressInRange) {
                    return Mono.error(new ValidationException("Address is out of range"));
                  }
                  return Mono.just(true);
                });

    Mono<DriverEntity> isDriverAvailableMono =
        driverService
            .findAvailableDriver(deliveryInfo)
            .switchIfEmpty(Mono.error(new ValidationException("No driver available")));

    Mono<VehicleEntity> isVehicleAvailableMono =
        vehicleService
            .findAvailableVehicle(deliveryInfo)
            .switchIfEmpty(Mono.error(new ValidationException("No vehicle available")));

    return Mono.zip(isAddressInRangeMono, isDriverAvailableMono, isVehicleAvailableMono)
        .map(tuple3 -> createDeliveryEntity(delivery, tuple3.getT2(), tuple3.getT3()))
        .flatMap(this::processDeliveryCreation);
  }

  @Override
  public Mono<Delivery> findById(String id) {
    return deliveryRepository.findById(new ObjectId(id)).map(DeliveryMapper::toDTO);
  }

  private DeliveryEntity createDeliveryEntity(
      Delivery delivery, DriverEntity driver, VehicleEntity vehicle) {
    var requestDelivery = DeliveryMapper.toEntity(delivery);
    var deliveryJob = new DeliveryJobEmbeddable(vehicle._id(), driver._id(), Instant.now(), null);

    return new DeliveryEntity(
        requestDelivery._id(),
        requestDelivery.orderId(),
        requestDelivery.deliveryInfo(),
        deliveryJob,
        LocalDateTime.now().plusHours(1),
        DeliveryStatus.INITIALIZING);
  }

  private Mono<Delivery> processDeliveryCreation(DeliveryEntity deliveryEntity) {
    return deliveryRepository.save(deliveryEntity).map(DeliveryMapper::toDTO);
  }
}
