package com.example.delivery.mapper;

import com.example.delivery.entity.DeliveryEntity;
import com.example.models.Delivery;
import java.util.Optional;
import org.bson.types.ObjectId;

public class DeliveryMapper {

  private DeliveryMapper() {}

  public static DeliveryEntity toEntity(Delivery delivery) {
    if (delivery == null) {
      return null;
    }
    return new DeliveryEntity(
        Optional.ofNullable(delivery.id()).map(ObjectId::new).orElse(null),
        delivery.orderId(),
        DeliveryInfoMapper.toEntity(delivery.deliveryInfo()));
  }

  public static Delivery toDTO(DeliveryEntity deliveryEntity) {
    if (deliveryEntity == null) {
      return null;
    }
    return new Delivery(
        deliveryEntity._id().toString(),
        deliveryEntity.orderId(),
        DeliveryInfoMapper.toDTO(deliveryEntity.deliveryInfo()),
        null);
  }
}
