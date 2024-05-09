package com.example.models;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Serdeable
public record ProductOrder(
    String id, @NotBlank String productId, @Positive int quantity, AuditData auditData) {

  public ProductOrder(String productId, int quantity) {
    this(null, productId, quantity, null);
  }
}
