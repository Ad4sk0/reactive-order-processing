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
import io.reactivex.rxjava3.core.Flowable;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false)
class DriverServiceImplTest {

  @Inject DriverService driverService;

  final DriverEntity driverEntity =
      new DriverEntity(new ObjectId(createObjectId("1")), "TestDriver1", DriverStatus.FREE);

  @Test
  void findAvailableDriver() {
    DeliveryInfo deliveryInfo = new DeliveryInfo("TestStreet", "TestCity");
    driverService.findAvailableDriver(deliveryInfo).test().assertValue(driverEntity);
  }

  @MockBean(DriverRepository.class)
  DriverRepository driverRepository() {
    DriverRepository driverRepository = mock(DriverRepository.class);
    Flowable<DriverEntity> driverEntityFlowable = Flowable.just(driverEntity);
    when(driverRepository.findFreeDrivers()).thenReturn(driverEntityFlowable);
    return driverRepository;
  }
}
