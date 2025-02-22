package com.example.delivery.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tests.TestsUtils.createObjectId;

import com.example.delivery.entity.DriverEntity;
import com.example.delivery.entity.DriverStatus;
import com.example.delivery.repository.DriverCustomRepository;
import com.example.delivery.repository.DriverRepository;
import com.example.models.DeliveryInfo;
import io.micronaut.test.annotation.MockBean;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class DriverServiceImplTest {

  DriverService driverService;

  @BeforeEach
  void init() {
    driverService =
        new DriverServiceImpl(createDriverRepositoryMock(), mock(DriverCustomRepository.class));
  }

  final DriverEntity driverEntity =
      new DriverEntity(new ObjectId(createObjectId("1")), "TestDriver1", DriverStatus.FREE);

  @Test
  void findAvailableDriver() {
    DeliveryInfo deliveryInfo = new DeliveryInfo("TestStreet", "TestCity");
    StepVerifier.create(driverService.isDriverAvailable(deliveryInfo))
        .expectNext(true)
        .verifyComplete();
  }

  @MockBean(DriverRepository.class)
  DriverRepository createDriverRepositoryMock() {
    DriverRepository driverRepository = mock(DriverRepository.class);
    when(driverRepository.findFreeDrivers()).thenReturn(Flux.just(driverEntity));
    return driverRepository;
  }
}
