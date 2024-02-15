package com.example.delivery.entity;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

@MappedEntity
public record DriverEntity(
    @Id @GeneratedValue ObjectId _id, @NotBlank String name, @NotNull DriverStatus status) {}
