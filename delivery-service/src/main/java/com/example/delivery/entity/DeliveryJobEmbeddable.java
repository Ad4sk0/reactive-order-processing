package com.example.delivery.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record DeliveryJobEmbeddable(
    @NotNull @Valid VehicleEntity vehicle,
    @NotNull @Valid DriverEntity driver,
    @NotNull Instant start,
    Instant end) {}
