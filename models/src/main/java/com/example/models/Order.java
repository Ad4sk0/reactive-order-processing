package com.example.models;

import io.micronaut.serde.annotation.Serdeable;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Serdeable
public class Order {
  private String id;
  private List<OrderItem> items;
  private DeliveryInfo deliveryInfo;
}
