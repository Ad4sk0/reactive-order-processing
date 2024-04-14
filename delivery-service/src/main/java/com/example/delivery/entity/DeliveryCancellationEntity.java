package com.example.delivery.entity;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

@MappedEntity
public record DeliveryCancellationEntity(@Id ObjectId _id, @NotNull ObjectId deliveryId) {}
