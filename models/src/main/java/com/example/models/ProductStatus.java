package com.example.models;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Min;

@Serdeable
public record ProductStatus(@Min(0) int quantity) {}
