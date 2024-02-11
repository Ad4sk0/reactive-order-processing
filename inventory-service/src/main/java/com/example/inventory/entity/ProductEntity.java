package com.example.inventory.entity;

import com.example.models.ProductType;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

@MappedEntity
public record ProductEntity(
    @Id @GeneratedValue ObjectId _id,
    @NotBlank String name,
    @NotNull ProductType productType,
    @Valid @NotNull ProductStatusEmbeddable status) {}
