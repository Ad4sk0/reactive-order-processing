package com.example.models;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.Instant;

@Serdeable
public record Delivery(
    String id,
    @NotBlank @Size(min = 24, max = 24, message = "OrderId must be exactly 24 characters long")
        String orderId,
    @NotNull @Valid DeliveryInfo deliveryInfo,
    Instant estimatedDeliveryTime,
    DeliveryStatus status,
    Instant start,
    Instant end) {}
