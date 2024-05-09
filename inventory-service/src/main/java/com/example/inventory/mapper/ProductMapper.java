package com.example.inventory.mapper;

import com.example.inventory.entity.ProductEntity;
import com.example.models.Product;

public class ProductMapper {

  private ProductMapper() {}

  public static ProductEntity toEntity(Product product) {
    if (product == null) {
      return null;
    }

    return ProductEntity.builder()
        .name(product.name())
        .productType(product.productType())
        .status(ProductStatusMapper.toEntity(product.status()))
        .build();
  }

  public static Product toDTO(ProductEntity productEntity) {
    if (productEntity == null) {
      return null;
    }
    return new Product(
        productEntity.getId().toString(),
        productEntity.getName(),
        productEntity.getProductType(),
        ProductStatusMapper.toDTO(productEntity.getStatus()),
        productEntity.getAuditData());
  }
}
