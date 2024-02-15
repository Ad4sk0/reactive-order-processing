package com.example.delivery.service;

import com.example.delivery.entity.DriverEntity;
import com.example.models.DeliveryInfo;
import io.reactivex.rxjava3.core.Maybe;

public interface DriverService {
  Maybe<DriverEntity> findAvailableDriver(DeliveryInfo deliveryInfo);
}
