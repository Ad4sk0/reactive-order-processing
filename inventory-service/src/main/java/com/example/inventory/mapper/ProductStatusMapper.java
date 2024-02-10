package com.example.inventory.mapper;

import com.example.inventory.entity.ProductStatusEntity;
import com.example.models.ProductStatus;
import java.util.Optional;
import org.bson.types.ObjectId;

public class ProductStatusMapper {

  private ProductStatusMapper() {}

  public static ProductStatusEntity toEntity(ProductStatus productStatus) {
    if (productStatus == null) {
      return null;
    }
    return new ProductStatusEntity(
        Optional.ofNullable(productStatus.id()).map(ObjectId::new).orElse(null),
        productStatus.quantity());
  }

  public static ProductStatus toDTO(ProductStatusEntity productStatusEntity) {
    if (productStatusEntity == null) {
      return null;
    }
    return new ProductStatus(productStatusEntity._id().toString(), productStatusEntity.quantity());
  }
}
