package com.example.inventory.service;

import com.example.models.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

  Flux<Product> findAll();

  Mono<Product> save(Product product);

  Mono<Product> findById(String id);
}
