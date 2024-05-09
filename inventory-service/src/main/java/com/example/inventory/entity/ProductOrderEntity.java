package com.example.inventory.entity;

import com.example.common.entity.BaseEntity;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedEntity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class ProductOrderEntity extends BaseEntity {
  private @NotBlank String productId;
  private @Positive int quantity;
}
