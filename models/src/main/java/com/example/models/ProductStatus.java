package com.example.models;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Serdeable
public record ProductStatus(@NotNull String id, @Min(0) int quantity) {}
