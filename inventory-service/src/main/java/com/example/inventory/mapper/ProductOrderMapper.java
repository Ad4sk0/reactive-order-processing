package com.example.inventory.mapper;

import com.example.inventory.entity.ProductOrderEntity;
import com.example.models.ProductOrder;

public class ProductOrderMapper {

  private ProductOrderMapper() {}

  public static ProductOrderEntity toEntity(ProductOrder productOrder) {
    if (productOrder == null) {
      return null;
    }
    return new ProductOrderEntity(
        productOrder.id(), productOrder.productId(), productOrder.quantity());
  }

  public static ProductOrder toDTO(ProductOrderEntity productOrderEntity) {
    if (productOrderEntity == null) {
      return null;
    }
    return new ProductOrder(
        productOrderEntity.id(), productOrderEntity.productId(), productOrderEntity.quantity());
  }
}
