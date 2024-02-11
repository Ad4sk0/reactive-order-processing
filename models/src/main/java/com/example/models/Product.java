package com.example.models;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record Product(
    String id, @NotBlank String name, ProductType productType, @Valid ProductStatus status) {
  public Product withStatus(ProductStatus updatedProductStatus) {
    return new Product(id, name, productType, updatedProductStatus);
  }
}
