package com.example.delivery.service;

import com.example.models.DeliveryInfo;
import io.reactivex.rxjava3.core.Single;

public interface AddressInRangeService {
  Single<Boolean> isAddressInRange(DeliveryInfo deliveryInfo);
}
