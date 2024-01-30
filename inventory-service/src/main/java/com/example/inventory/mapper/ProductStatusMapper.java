package com.example.inventory.mapper;

import com.example.inventory.entity.ProductStatusEntity;
import com.example.models.ProductStatus;

public class ProductStatusMapper {

  private ProductStatusMapper() {}

  public static ProductStatusEntity toEntity(ProductStatus productStatus) {
    if (productStatus == null) {
      return null;
    }
    return new ProductStatusEntity(productStatus.productId(), productStatus.quantity());
  }

  public static ProductStatus toDTO(ProductStatusEntity productStatusEntity) {
    if (productStatusEntity == null) {
      return null;
    }
    return new ProductStatus(productStatusEntity.productId(), productStatusEntity.quantity());
  }
}
