package com.example.delivery.mapper;

import com.example.delivery.entity.DeliveryEntity;
import com.example.models.Delivery;
import org.bson.types.ObjectId;

public class DeliveryMapper {

  private DeliveryMapper() {}

  public static DeliveryEntity toEntity(Delivery delivery) {
    if (delivery == null) {
      return null;
    }
    return new DeliveryEntity(
        null,
        new ObjectId(delivery.orderId()),
        DeliveryInfoMapper.toEntity(delivery.deliveryInfo()),
        null);
  }

  public static Delivery toDTO(DeliveryEntity deliveryEntity) {
    if (deliveryEntity == null) {
      return null;
    }
    return new Delivery(
        deliveryEntity._id().toString(),
        deliveryEntity.orderId().toString(),
        DeliveryInfoMapper.toDTO(deliveryEntity.deliveryInfo()),
        deliveryEntity.deliveryJobStatusEmbeddable().estimatedDeliveryTime(),
        deliveryEntity.deliveryJobStatusEmbeddable().status());
  }
}
