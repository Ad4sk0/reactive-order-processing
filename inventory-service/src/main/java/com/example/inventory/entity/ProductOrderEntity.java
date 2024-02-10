package com.example.inventory.entity;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.bson.types.ObjectId;

@MappedEntity
public record ProductOrderEntity(
        @Id @GeneratedValue ObjectId _id, @NotBlank String productId, @Positive int quantity) {}
