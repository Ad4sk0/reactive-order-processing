package com.example.delivery.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tests.TestsUtils.createObjectId;

import com.example.delivery.entity.*;
import com.example.delivery.repository.DeliveryRepository;
import com.example.models.Delivery;
import com.example.models.DeliveryInfo;
import com.example.models.DeliveryStatus;
import io.micronaut.context.event.ApplicationEventPublisher;
import jakarta.validation.ValidationException;
import java.time.Instant;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class DeliveryServiceImplTest {

  DeliveryService deliveryService;

  DeliveryLocationService deliveryLocationService;

  DriverService driverService;

  VehicleService vehicleService;

  @BeforeEach
  void init() {
    deliveryLocationService = createDeliveryLocationServiceMock();
    driverService = createDriverServiceMock();
    vehicleService = createVehicleService();

    deliveryService =
        new DeliveryServiceImpl(
            createDeliveryRepositoryMock(),
            deliveryLocationService,
            driverService,
            vehicleService,
            mock(ApplicationEventPublisher.class));
  }

  final DriverEntity driverEntity =
      new DriverEntity(new ObjectId(createObjectId("1")), "TestDriver1", DriverStatus.FREE);

  final VehicleEntity vehicleEntity =
      new VehicleEntity(new ObjectId(createObjectId("1")), "TestVehicle1", VehicleStatus.FREE);

  @Test
  void shouldCreateDeliveryWhenDeliveryIsPossible() {
    Delivery delivery =
        new Delivery(
            null,
            createObjectId("1"),
            new DeliveryInfo("Street1", "City1"),
            null,
            null,
            null,
            null);

    StepVerifier.create(deliveryService.save(delivery))
        .assertNext(
            d -> {
              assertNotNull(d.id());
              assertEquals("Street1", d.deliveryInfo().street());
              assertEquals("City1", d.deliveryInfo().city());
              assertEquals(DeliveryStatus.INITIALIZING, d.status());
              assertNotNull(d.estimatedDeliveryTime());
            })
        .verifyComplete();
  }

  @Test
  void shouldThrowExceptionIfIdIsProvided() {
    Delivery delivery =
        new Delivery(
            createObjectId("3"),
            createObjectId("1"),
            new DeliveryInfo("Street1", "City1"),
            null,
            null,
            null,
            null);
    StepVerifier.create(deliveryService.save(delivery))
        .expectError(UnsupportedOperationException.class)
        .verify();
  }

  @Test
  void shouldNotCreateDeliveryIfAddressIsNotInRange() {
    Delivery delivery =
        new Delivery(
            null,
            createObjectId("1"),
            new DeliveryInfo("Street1", "City1"),
            null,
            null,
            null,
            null);
    when(deliveryLocationService.getEstimatedDeliveryTime(any()))
        .thenReturn(Mono.error(new ValidationException("Address is not in range")));
    StepVerifier.create(deliveryService.save(delivery))
        .expectError(ValidationException.class)
        .verify();
  }

  @Test
  void shouldNotCreateDeliveryIfNoDriverAvailable() {
    Delivery delivery =
        new Delivery(
            null,
            createObjectId("1"),
            new DeliveryInfo("Street1", "City1"),
            null,
            null,
            null,
            null);
    when(driverService.findFirstFreeDriverAndChangeStatus(any())).thenReturn(Mono.empty());
    StepVerifier.create(deliveryService.save(delivery))
        .expectError(ValidationException.class)
        .verify();
  }

  @Test
  void shouldNotCreateDeliveryIfNoVehicleAvailable() {
    Delivery delivery =
        new Delivery(
            null,
            createObjectId("1"),
            new DeliveryInfo("Street1", "City1"),
            null,
            null,
            null,
            null);
    when(vehicleService.findFirstFreeVehicleAndChangeStatus(any())).thenReturn(Mono.empty());
    StepVerifier.create(deliveryService.save(delivery))
        .expectError(ValidationException.class)
        .verify();
  }

  DeliveryLocationService createDeliveryLocationServiceMock() {
    DeliveryLocationService deliveryLocationService = mock(DeliveryLocationService.class);
    when(deliveryLocationService.getEstimatedDeliveryTime(any()))
        .thenReturn(Mono.just(Instant.now().plusSeconds(60)));
    return deliveryLocationService;
  }

  DriverService createDriverServiceMock() {
    DriverService driverService = mock(DriverService.class);
    when(driverService.findFirstFreeDriverAndChangeStatus(any()))
        .thenReturn(Mono.just(driverEntity));
    return driverService;
  }

  VehicleService createVehicleService() {
    VehicleService vehicleService = mock(VehicleService.class);
    when(vehicleService.findFirstFreeVehicleAndChangeStatus(any()))
        .thenReturn(Mono.just(vehicleEntity));
    return vehicleService;
  }

  DeliveryRepository createDeliveryRepositoryMock() {
    DeliveryRepository deliveryRepository = mock(DeliveryRepository.class);
    when(deliveryRepository.save(any(DeliveryEntity.class)))
        .thenAnswer(
            invocation -> {
              DeliveryEntity deliveryEntity = invocation.getArgument(0);
              return Mono.just(
                  new DeliveryEntity(
                      new ObjectId(createObjectId("2")),
                      deliveryEntity.orderId(),
                      deliveryEntity.deliveryInfo(),
                      deliveryEntity.deliveryJobStatusEmbeddable()));
            });
    return deliveryRepository;
  }
}
