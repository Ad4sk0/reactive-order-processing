package com.example.order.controller;

import com.example.models.Order;
import com.example.order.service.OrderService;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller("/orders")
public class OrdersController {

  private final OrderService orderService;

  public OrdersController(OrderService orderService) {
    this.orderService = orderService;
  }

  @Get
  @SingleResult
  Mono<List<Order>> list() {
    return orderService.findAll().collectList();
  }

  @Post
  @Status(HttpStatus.CREATED)
  @SingleResult
  Mono<Order> save(@Valid @Body Order order) {
    return orderService.save(order);
  }

  @Get("/{id}")
  @SingleResult
  Mono<Order> find(@PathVariable String id) {
    return orderService.findById(id);
  }
}
