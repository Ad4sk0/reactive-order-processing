package com.example.inventory.entity;

import com.example.models.ProductType;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

@MappedEntity
public record ProductEntity(@Id @GeneratedValue String id, String name, ProductType productType) {}
