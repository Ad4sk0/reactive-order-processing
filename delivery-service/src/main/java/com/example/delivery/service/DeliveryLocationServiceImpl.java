package com.example.delivery.service;

import com.example.delivery.job.simulator.DeliveryJobStateUtils;
import com.example.models.DeliveryInfo;
import jakarta.inject.Singleton;
import jakarta.validation.ValidationException;
import java.time.Instant;
import reactor.core.publisher.Mono;

@Singleton
public class DeliveryLocationServiceImpl implements DeliveryLocationService {
  @Override
  public Mono<Boolean> isAddressInRange(DeliveryInfo deliveryInfo) {
    return Mono.fromCallable(() -> validateIfAddressInDeliveryRange(deliveryInfo));
  }

  @Override
  public Mono<Instant> getEstimatedDeliveryTime(DeliveryInfo deliveryInfo) {
    if (validateIfAddressInDeliveryRange(deliveryInfo)) {
      return Mono.just(Instant.now().plusSeconds(DeliveryJobStateUtils.getEstimatedDeliveryTime()));
    } else {
      return Mono.error(new ValidationException("Delivery address is out of range"));
    }
  }

  private boolean validateIfAddressInDeliveryRange(DeliveryInfo deliveryInfo) {
    return deliveryInfo.city().equals("New York");
  }
}
