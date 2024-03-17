package com.example.models;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.annotation.Nullable;

@Serdeable
public record ProductOrderPossibility(
    boolean isPossible, @Nullable ProductOrderPossibilityDetails details) {}
