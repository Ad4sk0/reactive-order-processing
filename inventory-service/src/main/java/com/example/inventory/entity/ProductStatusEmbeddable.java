package com.example.inventory.entity;

import io.micronaut.data.annotation.Embeddable;
import jakarta.validation.constraints.Min;

@Embeddable
public record ProductStatusEmbeddable(@Min(0) int quantity) {}
