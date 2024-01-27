package com.example.models;

import io.micronaut.serde.annotation.Serdeable;
import java.util.List;

@Serdeable
public record Product(
    String id, String name, ProductType productType) {}
