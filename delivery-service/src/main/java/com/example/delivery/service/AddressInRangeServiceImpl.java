package com.example.delivery.service;

import com.example.models.DeliveryInfo;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Singleton
public class AddressInRangeServiceImpl implements AddressInRangeService {
  @Override
  public Mono<Boolean> isAddressInRange(DeliveryInfo deliveryInfo) {
    return Mono.fromCallable(() -> validateIfAddressInDeliveryRange(deliveryInfo));
  }

  private boolean validateIfAddressInDeliveryRange(DeliveryInfo deliveryInfo) {
    return deliveryInfo.city().equals("New York");
  }
}
