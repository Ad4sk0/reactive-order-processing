package com.example.order.entity;

import com.example.common.entity.BaseEntity;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@MappedEntity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class OrderEntity extends BaseEntity {

  @Id @GeneratedValue private ObjectId id;

  @Valid @NotEmpty private List<OrderItemEmbeddable> orderItems;

  @Valid @NotNull private DeliveryInfoEmbeddable deliveryInfo;

  private ObjectId deliveryId;
}
