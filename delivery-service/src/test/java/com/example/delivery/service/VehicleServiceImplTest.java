package com.example.delivery.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tests.TestsUtils.createObjectId;

import com.example.delivery.entity.VehicleEntity;
import com.example.delivery.entity.VehicleStatus;
import com.example.delivery.repository.VehicleRepository;
import com.example.models.DeliveryInfo;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@MicronautTest(startApplication = false)
class VehicleServiceImplTest {

  @Inject VehicleService vehicleService;

  final VehicleEntity vehicleEntity =
      new VehicleEntity(new ObjectId(createObjectId("1")), "TestVehicle1", VehicleStatus.FREE);

  @Test
  void findAvailableVehicle() {
    DeliveryInfo deliveryInfo = new DeliveryInfo("TestStreet", "TestCity");
    StepVerifier.create(vehicleService.findAvailableVehicle(deliveryInfo))
        .expectNext(vehicleEntity)
        .verifyComplete();
  }

  @MockBean(VehicleRepository.class)
  VehicleRepository vehicleRepository() {
    VehicleRepository vehicleRepository = mock(VehicleRepository.class);
    when(vehicleRepository.findFreeVehicles()).thenReturn(Flux.just(vehicleEntity));
    return vehicleRepository;
  }
}
