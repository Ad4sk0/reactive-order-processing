package com.example.models;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Serdeable
public class Order {

  private String id;

  @NotEmpty @Valid private List<OrderItem> items;

  @NotNull @Valid private DeliveryInfo deliveryInfo;

  private String deliveryId;
}
