package com.example.order.service;

import com.example.models.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {

  Flux<Order> findAll();

  Mono<Order> save(Order order);

  Mono<Order> findById(String id);
}
