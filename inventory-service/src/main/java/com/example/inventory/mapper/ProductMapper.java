package com.example.inventory.mapper;

import com.example.inventory.entity.ProductEntity;
import com.example.models.Product;
import java.util.Optional;
import org.bson.types.ObjectId;

public class ProductMapper {

  private ProductMapper() {}

  public static ProductEntity toEntity(Product product) {
    if (product == null) {
      return null;
    }
    return new ProductEntity(
        Optional.ofNullable(product.id()).map(ObjectId::new).orElse(null),
        product.name(),
        product.productType(),
        ProductStatusMapper.toEntity(product.status()));
  }

  public static Product toDTO(ProductEntity productEntity) {
    if (productEntity == null) {
      return null;
    }
    return new Product(
        productEntity._id().toString(),
        productEntity.name(),
        productEntity.productType(),
        ProductStatusMapper.toDTO(productEntity.status()));
  }
}
