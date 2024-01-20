package com.example.models;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@Serdeable
public class Order {
  private Long id;
  private List<OrderItem> items;
  private DeliveryInfo deliveryInfo;
}
