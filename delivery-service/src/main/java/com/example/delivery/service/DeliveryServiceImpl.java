package com.example.delivery.service;

import com.example.delivery.entity.*;
import com.example.delivery.event.DeliveryCanceledEvent;
import com.example.delivery.event.DeliveryCreatedEvent;
import com.example.delivery.event.DeliveryEvent;
import com.example.delivery.job.DeliveryJobStatus;
import com.example.delivery.mapper.DeliveryCancellationMapper;
import com.example.delivery.mapper.DeliveryMapper;
import com.example.delivery.repository.*;
import com.example.models.*;
import io.micronaut.context.event.ApplicationEventPublisher;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import java.time.Instant;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Singleton
public class DeliveryServiceImpl implements DeliveryService {
  private static final Logger LOG = LoggerFactory.getLogger(DeliveryServiceImpl.class);
  private final DeliveryRepository deliveryRepository;
  private final DeliveryLocationService deliveryLocationService;
  private final DriverService driverService;
  private final VehicleService vehicleService;
  private final DeliveryCancellationRepository deliveryCancellationRepository;
  private final ApplicationEventPublisher<DeliveryEvent> eventPublisher;

  public DeliveryServiceImpl(
      DeliveryRepository deliveryRepository,
      DeliveryLocationService deliveryLocationService,
      DriverService driverService,
      VehicleService vehicleService,
      DeliveryCancellationRepository deliveryCancellationRepository,
      ApplicationEventPublisher<DeliveryEvent> eventPublisher) {
    this.deliveryRepository = deliveryRepository;
    this.deliveryLocationService = deliveryLocationService;
    this.driverService = driverService;
    this.vehicleService = vehicleService;
    this.eventPublisher = eventPublisher;
    this.deliveryCancellationRepository = deliveryCancellationRepository;
  }

  @Override
  public Flux<Delivery> findAll() {
    return deliveryRepository.findAll().map(DeliveryMapper::toDTO);
  }

  @Override
  @Transactional
  public Mono<Delivery> save(Delivery delivery) {
    LOG.info("Saving delivery for orderId {}", delivery.orderId());
    if (delivery.id() != null) {
      return Mono.error(new UnsupportedOperationException("Id should not be provided"));
    }
    DeliveryInfo deliveryInfo = delivery.deliveryInfo();

    Mono<Instant> estimatedDeliveryTimeMono =
        deliveryLocationService
            .getEstimatedDeliveryTime(deliveryInfo)
            .onErrorMap(throwable -> new ValidationException(throwable.getMessage()));

    Mono<DriverEntity> availableDriverMono =
        driverService
            .findFirstFreeDriverAndChangeStatus(DriverStatus.DURING_DELIVERY)
            .switchIfEmpty(Mono.error(new ValidationException("No driver available")));

    Mono<VehicleEntity> availableVehicleMono =
        vehicleService
            .findFirstFreeVehicleAndChangeStatus(VehicleStatus.DURING_DELIVERY)
            .switchIfEmpty(Mono.error(new ValidationException("No vehicle available")));

    return Mono.zip(estimatedDeliveryTimeMono, availableDriverMono, availableVehicleMono)
        .map(
            tuple3 ->
                createDeliveryEntity(delivery, tuple3.getT1(), tuple3.getT2(), tuple3.getT3()))
        .flatMap(this::processDeliveryCreation)
        .doOnSuccess(
            createdDelivery -> {
              LOG.info("Delivery created: {}", createdDelivery);
              eventPublisher.publishEvent(new DeliveryCreatedEvent(createdDelivery.id()));
            });
  }

  @Override
  public Mono<Delivery> findById(String id) {
    return deliveryRepository.findById(new ObjectId(id)).map(DeliveryMapper::toDTO);
  }

  @Override
  public Mono<Integer> updateStatusAndEstimatedDeliveryTime(DeliveryJobStatus deliveryJob) {
    return deliveryRepository.updateStatusAndEstimatedDeliveryTime(
        new ObjectId(deliveryJob.deliveryId()),
        deliveryJob.deliveryStatus(),
        deliveryJob.estimatedDeliveryTime());
  }

  @Override
  public Mono<Integer> completeDelivery(DeliveryJobStatus deliveryJobStatus) {

    Mono<Void> updateDriverAndVehicleStatusMono =
        getCompletedDelivery(deliveryJobStatus.deliveryId())
            .flatMap(
                deliveryEntity -> {
                  ObjectId driverId = deliveryEntity.deliveryJobStatusEmbeddable().driverId();
                  ObjectId vehicleId = deliveryEntity.deliveryJobStatusEmbeddable().vehicleId();
                  return Mono.zip(
                          updateDriverForCompletedDelivery(deliveryEntity, driverId),
                          updateVehicleForCompletedDelivery(deliveryEntity, vehicleId))
                      .then();
                });

    Mono<Integer> updateDeliveryEndTimeMono =
        deliveryRepository.updateEndTime(
            new ObjectId(deliveryJobStatus.deliveryId()), deliveryJobStatus.endTime());

    return Mono.zip(updateDriverAndVehicleStatusMono, updateDeliveryEndTimeMono).map(Tuple2::getT2);
  }

  @Override
  public Mono<DeliveryPossibility> isDeliveryPossible(DeliveryInfo deliveryInfo) {

    Mono<Boolean> isAddressInRangeMono = deliveryLocationService.isAddressInRange(deliveryInfo);
    Mono<Boolean> isDriverAvailableMono = driverService.isDriverAvailable(deliveryInfo);
    Mono<Boolean> isVehicleAvailableMono = vehicleService.isVehicleAvailable(deliveryInfo);

    return Mono.zip(isAddressInRangeMono, isDriverAvailableMono, isVehicleAvailableMono)
        .flatMap(
            tuple3 -> {
              boolean isAddressInRange = tuple3.getT1();
              boolean isDriverAvailable = tuple3.getT2();
              boolean isVehicleAvailable = tuple3.getT3();

              if (!isAddressInRange || !isDriverAvailable || !isVehicleAvailable) {
                var deliveryPossibilityDetails =
                    new DeliveryPossibilityDetails(
                        isAddressInRange, isVehicleAvailable, isDriverAvailable);
                return Mono.just(
                    new DeliveryPossibility(false, deliveryInfo, null, deliveryPossibilityDetails));
              }

              Mono<Instant> estimatedDeliveryTimeMono =
                  deliveryLocationService.getEstimatedDeliveryTime(deliveryInfo);
              return estimatedDeliveryTimeMono.map(
                  estimatedDeliveryTime ->
                      new DeliveryPossibility(true, deliveryInfo, estimatedDeliveryTime, null));
            });
  }

  @Override
  public Mono<DeliveryCancellation> cancelDelivery(DeliveryCancellation deliveryCancellation) {
    LOG.info("Delivery cancellation received for delivery {}", deliveryCancellation.deliveryId());
    return deliveryRepository
        .findById(new ObjectId(deliveryCancellation.deliveryId()))
        .switchIfEmpty(Mono.error(new IllegalStateException("Delivery not found")))
        .flatMap(
            deliveryEntity -> {
              LOG.info("Cancelling delivery id {}", deliveryEntity._id());
              if (deliveryEntity.deliveryJobStatusEmbeddable().status()
                  == DeliveryStatus.DELIVERED) {
                return Mono.error(
                    new IllegalStateException(
                        "Delivery can not be canceled because it has been already delivered"));
              }
              eventPublisher.publishEvent(
                  new DeliveryCanceledEvent(deliveryEntity._id().toString()));
              return Mono.just(deliveryEntity);
            })
        .flatMap(
            __ ->
                deliveryCancellationRepository.save(
                    DeliveryCancellationMapper.toEntity(deliveryCancellation)))
        .map(DeliveryCancellationMapper::toDTO);
  }

  private Mono<Object> updateVehicleForCompletedDelivery(
      DeliveryEntity deliveryEntity, ObjectId vehicleId) {
    return vehicleService
        .updateVehicleStatus(vehicleId, VehicleStatus.FREE)
        .map(
            updatedCount -> {
              if (updatedCount == 0) {
                return Mono.error(
                    new IllegalStateException(
                        String.format(
                            "Vehicle %s for completed delivery %s can not be updated",
                            vehicleId, deliveryEntity._id())));
              }
              return updatedCount;
            });
  }

  private Mono<Object> updateDriverForCompletedDelivery(
      DeliveryEntity deliveryEntity, ObjectId driverId) {
    return driverService
        .updateDriverStatus(driverId, DriverStatus.FREE)
        .map(
            updatedCount -> {
              if (updatedCount == 0) {
                return Mono.error(
                    new IllegalStateException(
                        String.format(
                            "Driver %s for completed delivery %s can not be updated",
                            driverId, deliveryEntity._id())));
              }
              return updatedCount;
            });
  }

  private Mono<DeliveryEntity> getCompletedDelivery(String deliveryId) {
    return deliveryRepository
        .findById(new ObjectId(deliveryId))
        .switchIfEmpty(
            Mono.error(
                new IllegalStateException(
                    "Delivery not found for job status with id: " + deliveryId)));
  }

  private DeliveryEntity createDeliveryEntity(
      Delivery delivery,
      Instant estimatedDeliveryTime,
      DriverEntity driver,
      VehicleEntity vehicle) {
    var requestDelivery = DeliveryMapper.toEntity(delivery);
    var deliveryJob =
        new DeliveryJobStatusEmbeddable(
            vehicle._id(),
            driver._id(),
            DeliveryStatus.INITIALIZING,
            Instant.now(),
            null,
            estimatedDeliveryTime);

    return new DeliveryEntity(
        requestDelivery._id(),
        requestDelivery.orderId(),
        requestDelivery.deliveryInfo(),
        deliveryJob);
  }

  private Mono<Delivery> processDeliveryCreation(DeliveryEntity deliveryEntity) {
    return deliveryRepository.save(deliveryEntity).map(DeliveryMapper::toDTO);
  }
}
