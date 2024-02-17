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
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Singleton;
import jakarta.validation.ValidationException;
import java.time.Instant;
import java.time.LocalDateTime;
import org.bson.types.ObjectId;

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
  public Flowable<Delivery> findAll() {
    return Flowable.fromPublisher(deliveryRepository.findAll()).map(DeliveryMapper::toDTO);
  }

  @Override
  public Single<Delivery> save(Delivery delivery) {
    if (delivery.id() != null) {
      return Single.error(new UnsupportedOperationException("Id should not be provided"));
    }
    DeliveryInfo deliveryInfo = delivery.deliveryInfo();

    Single<Boolean> isAddressInRangeSingle =
        addressInRangeService
            .isAddressInRange(deliveryInfo)
            .flatMap(
                addressInRange -> {
                  if (!addressInRange) {
                    return Single.error(new ValidationException("Address is out of range"));
                  }
                  return Single.just(true);
                });

    Single<DriverEntity> isDriverAvailableSingle =
        driverService
            .findAvailableDriver(deliveryInfo)
            .switchIfEmpty(Maybe.error(new ValidationException("No driver available")))
            .toSingle();

    Single<VehicleEntity> isVehicleAvailableSingle =
        vehicleService
            .findAvailableVehicle(deliveryInfo)
            .switchIfEmpty(Maybe.error(new ValidationException("No vehicle available")))
            .toSingle();

    return Single.zip(
            isAddressInRangeSingle,
            isDriverAvailableSingle,
            isVehicleAvailableSingle,
            (ignored, driver, vehicle) -> createDeliveryEntity(delivery, driver, vehicle))
        .flatMap(this::processDeliveryCreation);
  }

  @Override
  public Maybe<Delivery> findById(String id) {
    return Maybe.fromPublisher(deliveryRepository.findById(new ObjectId(id)))
        .map(DeliveryMapper::toDTO);
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

  private Single<Delivery> processDeliveryCreation(DeliveryEntity deliveryEntity) {
    return Single.fromPublisher(deliveryRepository.save(deliveryEntity)).map(DeliveryMapper::toDTO);
  }
}
