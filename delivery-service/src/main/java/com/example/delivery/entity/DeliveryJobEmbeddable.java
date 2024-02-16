package com.example.delivery.entity;

import io.micronaut.data.annotation.Embeddable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

@Embeddable
public record DeliveryJobEmbeddable(
    @NotNull @Valid VehicleEntity vehicle,
    @NotNull @Valid DriverEntity driver,
    @NotNull Instant start,
    Instant end) {}
