package com.example.models;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record OrderItem(String productId, String productName, int quantity) {}
