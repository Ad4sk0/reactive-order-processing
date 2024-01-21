package com.example.order.entity;

import io.micronaut.data.annotation.Embeddable;

@Embeddable
public record OrderItemEmbeddable(String productId, String productName, int quantity) {}
