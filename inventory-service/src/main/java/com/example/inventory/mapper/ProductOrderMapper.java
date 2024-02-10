package com.example.inventory.mapper;

import com.example.inventory.entity.ProductOrderEntity;
import com.example.models.ProductOrder;
import java.util.Optional;
import org.bson.types.ObjectId;

public class ProductOrderMapper {

  private ProductOrderMapper() {}

  public static ProductOrderEntity toEntity(ProductOrder productOrder) {
    if (productOrder == null) {
      return null;
    }
    return new ProductOrderEntity(
        Optional.ofNullable(productOrder.id()).map(ObjectId::new).orElse(null),
        productOrder.productId(),
        productOrder.quantity());
  }

  public static ProductOrder toDTO(ProductOrderEntity productOrderEntity) {
    if (productOrderEntity == null) {
      return null;
    }
    return new ProductOrder(
        productOrderEntity._id().toString(),
        productOrderEntity.productId(),
        productOrderEntity.quantity());
  }
}
