package com.example.delivery.entity;

import com.example.models.DeliveryStatus;
import io.micronaut.data.annotation.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import org.bson.types.ObjectId;

@Embeddable
public record DeliveryJobStatusEmbeddable(
    @NotNull ObjectId vehicleId,
    @NotNull ObjectId driverId,
    @NotNull DeliveryStatus status,
    @NotNull Instant start,
    Instant end,
    Instant estimatedDeliveryTime) {}
