package com.example.delivery.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.delivery.entity.*;
import com.example.delivery.repository.DeliveryRepository;
import com.example.models.Delivery;
import com.example.models.DeliveryInfo;
import com.example.models.DeliveryStatus;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import jakarta.validation.ValidationException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

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
    var createdDelivery = deliveryService.save(delivery).test();
    createdDelivery.assertValue(
        d -> {
          assertNotNull(d.id());
          assertEquals("Street1", d.deliveryInfo().street());
          assertEquals("City1", d.deliveryInfo().city());
          assertEquals(DeliveryStatus.INITIALIZING, d.status());
          assertNotNull(d.estimatedDeliveryTime());
          return true;
        });
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
    deliveryService.save(delivery).test().assertError(UnsupportedOperationException.class);
  }

  @Test
  void shouldNotCreateDeliveryIfAddressIsNotInRange() {
    Delivery delivery =
        new Delivery(null, createObjectId("1"), new DeliveryInfo("Street1", "City1"), null, null);
    when(addressInRangeService.isAddressInRange(any())).thenReturn(Single.just(false));
    deliveryService.save(delivery).test().assertError(ValidationException.class);
  }

  @Test
  void shouldNotCreateDeliveryIfNoDriverAvailable() {
    Delivery delivery =
        new Delivery(null, createObjectId("1"), new DeliveryInfo("Street1", "City1"), null, null);
    when(driverService.findAvailableDriver(any())).thenReturn(Maybe.empty());
    deliveryService.save(delivery).test().assertError(ValidationException.class);
  }

  @Test
  void shouldNotCreateDeliveryIfNoVehicleAvailable() {
    Delivery delivery =
        new Delivery(null, createObjectId("1"), new DeliveryInfo("Street1", "City1"), null, null);
    when(vehicleService.findAvailableVehicle(any())).thenReturn(Maybe.empty());
    deliveryService.save(delivery).test().assertError(ValidationException.class);
  }

  @MockBean(AddressInRangeService.class)
  AddressInRangeService addressInRangeService() {
    AddressInRangeService addressInRangeService = mock(AddressInRangeService.class);
    when(addressInRangeService.isAddressInRange(any())).thenReturn(Single.just(true));
    return addressInRangeService;
  }

  @MockBean(DriverService.class)
  DriverService driverService() {
    DriverService driverService = mock(DriverService.class);
    when(driverService.findAvailableDriver(any())).thenReturn(Maybe.just(driverEntity));
    return driverService;
  }

  @MockBean(VehicleService.class)
  VehicleService vehicleService() {
    VehicleService vehicleService = mock(VehicleService.class);
    when(vehicleService.findAvailableVehicle(any())).thenReturn(Maybe.just(vehicleEntity));
    return vehicleService;
  }

  @MockBean(DeliveryRepository.class)
  DeliveryRepository deliveryRepository() {
    DeliveryRepository deliveryRepository = mock(DeliveryRepository.class);
    when(deliveryRepository.save(any(DeliveryEntity.class)))
        .thenAnswer(
            invocation -> {
              DeliveryEntity deliveryEntity = invocation.getArgument(0);
              return Flowable.just(
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

  private String createObjectId(String id) {
    if (id.length() > 24) {
      throw new IllegalArgumentException("Id length must be 24 characters or less.");
    }
    StringBuilder sb = new StringBuilder(id);
    while (sb.length() < 24) {
      sb.insert(0, "0");
    }
    return sb.toString();
  }
}
