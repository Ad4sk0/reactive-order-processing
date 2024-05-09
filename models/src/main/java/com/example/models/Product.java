package com.example.models;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record Product(
    String id,
    @NotBlank String name,
    ProductType productType,
    @Valid ProductStatus status,
    AuditData auditData) {

  public Product(String name, ProductType productType, ProductStatus status) {
    this(null, name, productType, status, null);
  }

  public Product withStatus(ProductStatus updatedProductStatus) {
    return new Product(id, name, productType, updatedProductStatus, auditData);
  }
}
