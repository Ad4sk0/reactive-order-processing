package com.example.inventory.mapper;

import com.example.inventory.entity.ProductStatusEmbeddable;
import com.example.models.ProductStatus;

public class ProductStatusMapper {

  private ProductStatusMapper() {}

  public static ProductStatusEmbeddable toEntity(ProductStatus productStatus) {
    if (productStatus == null) {
      return null;
    }
    return new ProductStatusEmbeddable(productStatus.quantity());
  }

  public static ProductStatus toDTO(ProductStatusEmbeddable productStatusEmbeddable) {
    if (productStatusEmbeddable == null) {
      return null;
    }
    return new ProductStatus(productStatusEmbeddable.quantity());
  }
}
