package com.example.inventory.entity;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.constraints.Positive;
import org.bson.types.ObjectId;

@MappedEntity
public record ProductStatusEntity(@Id ObjectId id, @Positive int quantity) {}
