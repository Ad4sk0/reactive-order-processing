package com.example.order.entity;

import io.micronaut.data.annotation.Embeddable;

@Embeddable
public record DeliveryInfoEmbeddable(String street, String city) {}
