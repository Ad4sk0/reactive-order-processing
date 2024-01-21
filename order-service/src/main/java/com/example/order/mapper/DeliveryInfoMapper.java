package com.example.order.mapper;

import com.example.models.DeliveryInfo;
import com.example.order.entity.DeliveryInfoEmbeddable;

public class DeliveryInfoMapper {

  private DeliveryInfoMapper() {}

  public static DeliveryInfoEmbeddable toEntity(DeliveryInfo deliveryInfo) {
    if (deliveryInfo == null) {
      return null;
    }
    return new DeliveryInfoEmbeddable(deliveryInfo.street(), deliveryInfo.city());
  }

  public static DeliveryInfo toDTO(DeliveryInfoEmbeddable deliveryInfoEmbeddable) {
    if (deliveryInfoEmbeddable == null) {
      return null;
    }
    return new DeliveryInfo(deliveryInfoEmbeddable.street(), deliveryInfoEmbeddable.city());
  }
}
