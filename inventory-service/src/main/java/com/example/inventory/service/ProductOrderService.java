package com.example.inventory.service;

import com.example.models.ProductOrder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductOrderService {

  Flux<ProductOrder> findAll();

  Mono<ProductOrder> save(ProductOrder product);

  Mono<ProductOrder> findById(String id);
}
