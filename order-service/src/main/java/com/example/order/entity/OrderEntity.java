package com.example.order.entity;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@MappedEntity
@Data
@Builder
public class OrderEntity {

  @Id @GeneratedValue private String id;

  private List<OrderItemEmbeddable> orderItems;

  private DeliveryInfoEmbeddable deliveryInfo;
}
