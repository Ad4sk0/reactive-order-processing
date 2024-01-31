package com.example.models;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record ProductOrderCancellation(@NotBlank String productOrderId) {}
