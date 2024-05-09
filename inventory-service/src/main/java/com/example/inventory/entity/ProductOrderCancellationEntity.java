package com.example.inventory.entity;

import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@MappedEntity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class ProductOrderCancellationEntity extends BaseEntity {
  @NotNull ObjectId productOrderId;
}
