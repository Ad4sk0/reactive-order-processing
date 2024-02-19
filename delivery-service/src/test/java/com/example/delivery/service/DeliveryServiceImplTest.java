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
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.validation.ValidationException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@MicronautTest(startApplication = false)
class DeliveryServiceImplTest {

  @Inject DeliveryService deliveryService;

  @Inject AddressInRangeService addressInRangeService;

  @Inject DriverService driverService;

  @Inject VehicleService vehicleService;

  final DriverEntity driverEntity =
      new DriverEntity(new ObjectId(createObjectId("1")), "TestDriver1", DriverStatus.FREE);

  final VehicleEntity vehicleEntity =
      new VehicleEntity(new ObjectId(createObjectId("1")), "TestVehicle1", VehicleStatus.FREE);

  @Test
  void shouldCreateDeliveryWhenDeliveryIsPossible() {
    Delivery delivery =
        new Delivery(null, createObjectId("1"), new DeliveryInfo("Street1", "City1"), null, null);

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
            null);
    StepVerifier.create(deliveryService.save(delivery))
        .expectError(UnsupportedOperationException.class)
        .verify();
  }

  @Test
  void shouldNotCreateDeliveryIfAddressIsNotInRange() {
    Delivery delivery =
        new Delivery(null, createObjectId("1"), new DeliveryInfo("Street1", "City1"), null, null);
    when(addressInRangeService.isAddressInRange(any())).thenReturn(Mono.just(false));
    StepVerifier.create(deliveryService.save(delivery))
        .expectError(ValidationException.class)
        .verify();
  }

  @Test
  void shouldNotCreateDeliveryIfNoDriverAvailable() {
    Delivery delivery =
        new Delivery(null, createObjectId("1"), new DeliveryInfo("Street1", "City1"), null, null);
    when(driverService.findAvailableDriver(any())).thenReturn(Mono.empty());
    StepVerifier.create(deliveryService.save(delivery))
        .expectError(ValidationException.class)
        .verify();
  }

  @Test
  void shouldNotCreateDeliveryIfNoVehicleAvailable() {
    Delivery delivery =
        new Delivery(null, createObjectId("1"), new DeliveryInfo("Street1", "City1"), null, null);
    when(vehicleService.findAvailableVehicle(any())).thenReturn(Mono.empty());
    StepVerifier.create(deliveryService.save(delivery))
        .expectError(ValidationException.class)
        .verify();
  }

  @MockBean(AddressInRangeService.class)
  AddressInRangeService addressInRangeService() {
    AddressInRangeService addressInRangeService = mock(AddressInRangeService.class);
    when(addressInRangeService.isAddressInRange(any())).thenReturn(Mono.just(true));
    return addressInRangeService;
  }

  @MockBean(DriverService.class)
  DriverService driverService() {
    DriverService driverService = mock(DriverService.class);
    when(driverService.findAvailableDriver(any())).thenReturn(Mono.just(driverEntity));
    return driverService;
  }

  @MockBean(VehicleService.class)
  VehicleService vehicleService() {
    VehicleService vehicleService = mock(VehicleService.class);
    when(vehicleService.findAvailableVehicle(any())).thenReturn(Mono.just(vehicleEntity));
    return vehicleService;
  }

  @MockBean(DeliveryRepository.class)
  DeliveryRepository deliveryRepository() {
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
                      deliveryEntity.deliveryJobEmbeddable(),
                      deliveryEntity.estimatedDeliveryTime(),
                      deliveryEntity.status()));
            });
    return deliveryRepository;
  }
}
