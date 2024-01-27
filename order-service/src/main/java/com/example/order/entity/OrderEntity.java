package com.example.order.entity;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@MappedEntity
@Data
@Builder
public class OrderEntity {

  @Id @GeneratedValue private String id;

  @Valid @NotEmpty private List<OrderItemEmbeddable> orderItems;

  @Valid @NotNull private DeliveryInfoEmbeddable deliveryInfo;
}
