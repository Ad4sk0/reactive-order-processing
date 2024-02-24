package com.example.delivery.job;

import com.example.models.DeliveryStatus;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

@Serdeable
public record DeliveryJobStatus(
    @NotBlank @Size(min = 24, max = 24, message = "deliverId must be exactly 24 characters long")
        String deliveryId,
    @NotNull DeliveryStatus deliveryStatus,
    Instant estimatedDeliveryTime,
    Instant endTime) {}
