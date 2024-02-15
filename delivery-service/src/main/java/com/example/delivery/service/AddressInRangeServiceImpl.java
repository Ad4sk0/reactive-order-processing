package com.example.delivery.service;

import com.example.models.DeliveryInfo;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Singleton;

@Singleton
public class AddressInRangeServiceImpl implements AddressInRangeService {
  @Override
  public Single<Boolean> isAddressInRange(DeliveryInfo deliveryInfo) {
    return Single.fromCallable(() -> validateIfAddressInDeliveryRange(deliveryInfo));
  }

  private boolean validateIfAddressInDeliveryRange(DeliveryInfo deliveryInfo) {
    return deliveryInfo.city().equals("New York");
  }
}
