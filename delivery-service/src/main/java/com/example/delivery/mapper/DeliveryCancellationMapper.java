package com.example.delivery.mapper;

import com.example.delivery.entity.DeliveryCancellationEntity;
import com.example.models.DeliveryCancellation;
import java.util.Optional;
import org.bson.types.ObjectId;

public class DeliveryCancellationMapper {

  private DeliveryCancellationMapper() {}

  public static DeliveryCancellationEntity toEntity(DeliveryCancellation deliveryCancellation) {
    if (deliveryCancellation == null) {
      return null;
    }
    return new DeliveryCancellationEntity(null, new ObjectId(deliveryCancellation.deliveryId()));
  }

  public static DeliveryCancellation toDTO(DeliveryCancellationEntity deliveryCancellationEntity) {
    if (deliveryCancellationEntity == null) {
      return null;
    }
    return new DeliveryCancellation(
        Optional.of(deliveryCancellationEntity._id()).map(ObjectId::toString).orElse(null),
        deliveryCancellationEntity.deliveryId().toString());
  }
}
