package com.example.models;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record ProductOrder(String id, String productId, int quantity) {}
