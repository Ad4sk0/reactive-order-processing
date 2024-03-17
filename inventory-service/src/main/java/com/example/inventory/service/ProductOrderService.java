package com.example.inventory.service;

import com.example.models.ProductOrder;
import com.example.models.ProductOrderPossibility;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductOrderService {

  Flux<ProductOrder> findAll();

  Mono<ProductOrder> save(ProductOrder product);

  Mono<ProductOrder> findById(String id);

  Mono<ProductOrderPossibility> isProductOrderPossible(List<ProductOrder> products);
}
