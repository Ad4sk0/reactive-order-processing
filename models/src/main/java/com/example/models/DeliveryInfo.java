package com.example.models;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record DeliveryInfo(@NotBlank String street, @NotBlank String city) {}
