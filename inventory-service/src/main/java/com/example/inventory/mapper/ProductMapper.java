package com.example.inventory.mapper;

import com.example.inventory.entity.ProductEntity;
import com.example.models.Product;

public class ProductMapper {

  private ProductMapper() {}

  public static ProductEntity toEntity(Product product) {
    if (product == null) {
      return null;
    }
    return new ProductEntity(product.id(), product.name(), product.productType());
  }

  public static Product toDTO(ProductEntity productEntity) {
    if (productEntity == null) {
      return null;
    }
    return new Product(productEntity.id(), productEntity.name(), productEntity.productType());
  }
}
