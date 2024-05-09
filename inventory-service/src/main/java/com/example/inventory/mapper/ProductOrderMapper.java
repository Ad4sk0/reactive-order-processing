package com.example.inventory.mapper;

import com.example.inventory.entity.ProductOrderEntity;
import com.example.models.ProductOrder;

public class ProductOrderMapper {

  private ProductOrderMapper() {}

  public static ProductOrderEntity toEntity(ProductOrder productOrder) {
    if (productOrder == null) {
      return null;
    }
    return ProductOrderEntity.builder()
        .productId(productOrder.productId())
        .quantity(productOrder.quantity())
        .build();
  }

  public static ProductOrder toDTO(ProductOrderEntity productOrderEntity) {
    if (productOrderEntity == null) {
      return null;
    }
    return new ProductOrder(
        productOrderEntity.getId().toString(),
        productOrderEntity.getProductId(),
        productOrderEntity.getQuantity(),
        productOrderEntity.getAuditData());
  }
}
