package com.example.inventory.entity;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

@MappedEntity
public record ProductOrderCancellationEntity(
    @Id @GeneratedValue ObjectId _id, @NotNull ObjectId productOrderId) {}
