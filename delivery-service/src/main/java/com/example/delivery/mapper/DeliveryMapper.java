package com.example.delivery.mapper;

import com.example.delivery.entity.DeliveryEntity;
import com.example.models.Delivery;

public class DeliveryMapper {

  private DeliveryMapper() {}

  public static DeliveryEntity toEntity(Delivery delivery) {
    if (delivery == null) {
      return null;
    }
    return new DeliveryEntity(
        null, delivery.orderId(), DeliveryInfoMapper.toEntity(delivery.deliveryInfo()), null, null);
  }

  public static Delivery toDTO(DeliveryEntity deliveryEntity) {
    if (deliveryEntity == null) {
      return null;
    }
    return new Delivery(
        deliveryEntity._id().toString(),
        deliveryEntity.orderId(),
        DeliveryInfoMapper.toDTO(deliveryEntity.deliveryInfo()),
        deliveryEntity.estimatedDeliveryTime(),
        deliveryEntity.status());
  }
}
