package com.example.models;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record ProductOrderCancellation(String productOrderId) {}
