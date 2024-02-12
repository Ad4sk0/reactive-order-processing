package com.example.models;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record DeliveryPossibilityDetails(
    Boolean isAddressInRange, Boolean isVehicleAvailable, Boolean isDriverAvailable) {}
