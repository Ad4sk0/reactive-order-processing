package com.example.models;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import java.time.Instant;

@Serdeable
public record DeliveryPossibility(
    @NotEmpty boolean isDeliveryPossible,
    @NotEmpty @Valid DeliveryInfo deliveryInfo,
    @Future Instant estimatedDeliveryTime,
    DeliveryPossibilityDetails deliveryPossibilityDetails) {}
