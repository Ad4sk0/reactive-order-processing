package com.example.inventory.entity;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

@MappedEntity
public record ProductStatusEntity(@Id String productId, int quantity) {}
