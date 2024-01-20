package com.example.models;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record Ingredient(Long id, String name, boolean isAvailable) {}
