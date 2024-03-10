package com.example.order.controller;

import com.example.models.Order;
import com.example.order.service.OrderService;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller("/orders")
public class OrdersController {

  private final OrderService orderService;

  public OrdersController(OrderService orderService) {
    this.orderService = orderService;
  }

  @Get
  Flux<Order> list() {
    return orderService.findAll();
  }

  @Post
  @Status(HttpStatus.CREATED)
  @SingleResult
  Mono<Order> save(@Valid @Body Order order) {
    return orderService.save(order);
  }

  @Put
  @SingleResult
  Mono<Order> update(@Valid @Body Order order) {
    return orderService.save(order);
  }

  @Get("/{id}")
  @SingleResult
  Mono<Order> find(@PathVariable String id) {
    return orderService.findById(id);
  }
}
