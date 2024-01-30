package com.example.inventory.entity;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

@MappedEntity
public record ProductOrderEntity(@Id @GeneratedValue String id, String productId, int quantity) {}
