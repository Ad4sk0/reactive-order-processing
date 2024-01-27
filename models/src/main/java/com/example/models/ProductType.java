package com.example.models;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public enum ProductType {
  PIZZA,
  PASTA,
  DRINK,
}
