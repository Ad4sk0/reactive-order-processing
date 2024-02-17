package com.example.delivery.entity;

import io.micronaut.data.annotation.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import org.bson.types.ObjectId;

@Embeddable
public record DeliveryJobEmbeddable(
    @NotNull ObjectId vehicleId, @NotNull ObjectId driverId, @NotNull Instant start, Instant end) {}
