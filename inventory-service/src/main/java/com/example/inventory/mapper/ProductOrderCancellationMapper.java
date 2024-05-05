package com.example.inventory.mapper;

import com.example.inventory.entity.ProductOrderCancellationEntity;
import com.example.models.ProductOrderCancellation;
import java.util.Optional;
import org.bson.types.ObjectId;

public class ProductOrderCancellationMapper {

  private ProductOrderCancellationMapper() {}

  public static ProductOrderCancellationEntity toEntity(
      ProductOrderCancellation productOrderCancellation) {
    if (productOrderCancellation == null) {
      return null;
    }
    return new ProductOrderCancellationEntity(
        Optional.ofNullable(productOrderCancellation.id()).map(ObjectId::new).orElse(null),
        Optional.ofNullable(productOrderCancellation.productOrderId())
            .map(ObjectId::new)
            .orElse(null));
  }

  public static ProductOrderCancellation toDTO(
      ProductOrderCancellationEntity productOrderCancellationEntity) {
    if (productOrderCancellationEntity == null) {
      return null;
    }
    return new ProductOrderCancellation(
        productOrderCancellationEntity._id().toString(),
        productOrderCancellationEntity._id().toString());
  }
}
