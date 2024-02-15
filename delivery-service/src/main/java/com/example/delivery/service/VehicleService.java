package com.example.delivery.service;

import com.example.delivery.entity.VehicleEntity;
import com.example.models.DeliveryInfo;
import io.reactivex.rxjava3.core.Maybe;

public interface VehicleService {
  Maybe<VehicleEntity> findAvailableVehicle(DeliveryInfo deliveryInfo);
}
