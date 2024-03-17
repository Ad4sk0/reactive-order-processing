package com.example.models;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record ProductOrderPossibilityDetails(@Nullable ProductOrderPossibilityErrorReason reason) {}
