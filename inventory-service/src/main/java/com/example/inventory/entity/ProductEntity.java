package com.example.inventory.entity;

import com.example.models.ProductType;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedEntity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class ProductEntity extends BaseEntity {
  private @NotBlank String name;
  private @NotNull ProductType productType;
  private @Valid @NotNull ProductStatusEmbeddable status;
}
