package com.example.models;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Serdeable
public record Order (String id, @NotEmpty @Valid List<OrderItem> items, @NotNull @Valid DeliveryInfo deliveryInfo, String deliveryId, AuditData auditData) {

  public Order(List<OrderItem> items, DeliveryInfo deliveryInfo) {
    this(null, items, deliveryInfo, null, null);
  }

}
