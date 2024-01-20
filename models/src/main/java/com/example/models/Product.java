package com.example.models;

import io.micronaut.serde.annotation.Serdeable;
import java.util.List;

@Serdeable
public record Product(Long id, String name, boolean isAvailable, List<Ingredient> ingredients) {}
