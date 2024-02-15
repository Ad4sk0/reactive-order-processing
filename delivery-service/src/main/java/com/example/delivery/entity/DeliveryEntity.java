package com.example.delivery.entity;

import com.example.models.DeliveryStatus;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.bson.types.ObjectId;

@MappedEntity
public record DeliveryEntity(
    @Id @GeneratedValue ObjectId _id,
    @NotBlank String orderId,
    @NotNull @Valid DeliveryInfoEmbeddable deliveryInfo,
    @NotNull @Valid DeliveryJobEmbeddable deliveryJobEmbeddable,
    @NotNull LocalDateTime estimatedDeliveryTime,
    @NotNull DeliveryStatus status) {}
