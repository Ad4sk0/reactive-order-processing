package com.example.delivery.service;

import com.example.models.DeliveryInfo;
import java.time.Instant;
import reactor.core.publisher.Mono;

public interface DeliveryLocationService {
  Mono<Boolean> isAddressInRange(DeliveryInfo deliveryInfo);

  Mono<Instant> getEstimatedDeliveryTime(DeliveryInfo deliveryInfo);
}
