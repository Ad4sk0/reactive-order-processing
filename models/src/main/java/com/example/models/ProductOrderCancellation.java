package com.example.models;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Serdeable
public record ProductOrderCancellation(
    String id,
    @NotBlank
        @Size(min = 24, max = 24, message = "productOrderId must be exactly 24 characters long")
        String productOrderId,
    AuditData auditData) {

  public ProductOrderCancellation(String productOrderId) {
    this(null, productOrderId, null);
  }
}
