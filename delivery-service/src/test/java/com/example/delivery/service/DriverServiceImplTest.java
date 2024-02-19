package com.example.delivery.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tests.TestsUtils.createObjectId;

import com.example.delivery.entity.DriverEntity;
import com.example.delivery.entity.DriverStatus;
import com.example.delivery.repository.DriverRepository;
import com.example.models.DeliveryInfo;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@MicronautTest(startApplication = false)
class DriverServiceImplTest {

  @Inject DriverService driverService;

  final DriverEntity driverEntity =
      new DriverEntity(new ObjectId(createObjectId("1")), "TestDriver1", DriverStatus.FREE);

  @Test
  void findAvailableDriver() {
    DeliveryInfo deliveryInfo = new DeliveryInfo("TestStreet", "TestCity");
    StepVerifier.create(driverService.findAvailableDriver(deliveryInfo))
        .expectNext(driverEntity)
        .verifyComplete();
  }

  @MockBean(DriverRepository.class)
  DriverRepository driverRepository() {
    DriverRepository driverRepository = mock(DriverRepository.class);
    when(driverRepository.findFreeDrivers()).thenReturn(Flux.just(driverEntity));
    return driverRepository;
  }
}
