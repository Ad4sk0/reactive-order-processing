package com.example.order.entity;

import io.micronaut.data.annotation.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Embeddable
public record OrderItemEmbeddable(
    @NotBlank String productId, @Positive int quantity) {}
