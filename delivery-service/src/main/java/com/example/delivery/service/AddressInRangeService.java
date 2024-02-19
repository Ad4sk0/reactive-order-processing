package com.example.delivery.service;

import com.example.models.DeliveryInfo;
import reactor.core.publisher.Mono;

public interface AddressInRangeService {
  Mono<Boolean> isAddressInRange(DeliveryInfo deliveryInfo);
}
