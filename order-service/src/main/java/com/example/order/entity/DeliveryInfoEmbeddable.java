package com.example.order.entity;

import io.micronaut.data.annotation.Embeddable;
import jakarta.validation.constraints.NotBlank;

@Embeddable
public record DeliveryInfoEmbeddable(@NotBlank String street, @NotBlank String city) {}
