package com.example.models;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Serdeable
public record OrderItem(
    @NotBlank String productId, @NotBlank String productName, @Positive int quantity) {}
