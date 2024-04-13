package com.example.models;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Serdeable
public record DeliveryCancellation(
    String _id,
    @NotBlank @Size(min = 24, max = 24, message = "DeliveryId must be exactly 24 characters long")
        String deliveryId) {}
