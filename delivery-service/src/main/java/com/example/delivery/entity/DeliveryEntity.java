package com.example.delivery.entity;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

@MappedEntity
public record DeliveryEntity(
    @Id @GeneratedValue ObjectId _id,
    @NotNull ObjectId orderId,
    @NotNull @Valid DeliveryInfoEmbeddable deliveryInfo,
    @NotNull @Valid DeliveryJobStatusEmbeddable deliveryJobStatusEmbeddable) {}
