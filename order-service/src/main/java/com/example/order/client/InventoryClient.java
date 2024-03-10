package com.example.order.client;

import com.example.models.Product;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Flux;

@Client(id = "inventory")
public interface InventoryClient {

  @Get("/products")
  Flux<Product> fetchProducts();
}
