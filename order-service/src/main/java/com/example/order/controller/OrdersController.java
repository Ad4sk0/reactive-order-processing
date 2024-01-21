package com.example.order.controller;

import com.example.models.Order;
import com.example.order.service.OrderService;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

@Controller("/orders")
public class OrdersController {

  private final OrderService orderService;

  public OrdersController(OrderService orderService) {
    this.orderService = orderService;
  }

  @Get
  Flowable<Order> list() {
    return orderService.findAll();
  }

  @Post
  @Status(HttpStatus.CREATED)
  @SingleResult
  Single<Order> save(Order order) {
    return orderService.save(order);
  }

  @Put
  @SingleResult
  Single<Order> update(Order order) {
    return orderService.save(order);
  }

  @Get("/{id}")
  @SingleResult
  Maybe<Order> find(@PathVariable String id) {
    return orderService.findById(id);
  }
}
