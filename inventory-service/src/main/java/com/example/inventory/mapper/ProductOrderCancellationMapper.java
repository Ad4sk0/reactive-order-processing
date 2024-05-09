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

    return ProductOrderCancellationEntity.builder()
        .productOrderId(
            Optional.ofNullable(productOrderCancellation.productOrderId())
                .map(ObjectId::new)
                .orElse(null))
        .build();
  }

  public static ProductOrderCancellation toDTO(
      ProductOrderCancellationEntity productOrderCancellationEntity) {
    if (productOrderCancellationEntity == null) {
      return null;
    }
    return new ProductOrderCancellation(
        productOrderCancellationEntity.getId().toString(),
        productOrderCancellationEntity.getProductOrderId().toString(),
        productOrderCancellationEntity.getAuditData());
  }
}
