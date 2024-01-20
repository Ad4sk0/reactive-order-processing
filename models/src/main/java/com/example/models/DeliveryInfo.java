package com.example.models;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record DeliveryInfo(String street, String city) {
}
