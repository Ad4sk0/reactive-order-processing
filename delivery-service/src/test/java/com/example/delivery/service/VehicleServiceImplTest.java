package com.example.delivery.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tests.TestsUtils.createObjectId;

import com.example.delivery.entity.VehicleEntity;
import com.example.delivery.entity.VehicleStatus;
import com.example.delivery.repository.VehicleCustomRepository;
import com.example.delivery.repository.VehicleRepository;
import com.example.models.DeliveryInfo;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class VehicleServiceImplTest {

  VehicleService vehicleService;

  @BeforeEach
  void init() {
    vehicleService =
        new VehicleServiceImpl(createVehicleRepositoryMock(), mock(VehicleCustomRepository.class));
  }

  final VehicleEntity vehicleEntity =
      new VehicleEntity(new ObjectId(createObjectId("1")), "TestVehicle1", VehicleStatus.FREE);

  @Test
  void findAvailableVehicle() {
    DeliveryInfo deliveryInfo = new DeliveryInfo("TestStreet", "TestCity");
    StepVerifier.create(vehicleService.isVehicleAvailable(deliveryInfo))
        .expectNext(true)
        .verifyComplete();
  }

  VehicleRepository createVehicleRepositoryMock() {
    VehicleRepository vehicleRepository = mock(VehicleRepository.class);
    when(vehicleRepository.findFreeVehicles()).thenReturn(Flux.just(vehicleEntity));
    return vehicleRepository;
  }
}
