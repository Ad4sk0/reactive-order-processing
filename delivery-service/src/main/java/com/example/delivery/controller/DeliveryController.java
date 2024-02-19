package com.example.delivery.controller;

import com.example.delivery.service.DeliveryService;
import com.example.models.Delivery;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller("/deliveries")
public class DeliveryController {

  private final DeliveryService deliveryService;

  public DeliveryController(DeliveryService deliveryService) {
    this.deliveryService = deliveryService;
  }

  @Get
  Flux<Delivery> list() {
    return deliveryService.findAll();
  }

  @Post
  @Status(HttpStatus.CREATED)
  @SingleResult
  Mono<Delivery> save(@Valid @Body Delivery delivery) {
    return deliveryService.save(delivery);
  }

  @Put
  @SingleResult
  Mono<Delivery> update(@Valid @Body Delivery delivery) {
    return deliveryService.save(delivery);
  }

  @Get("/{id}")
  @SingleResult
  Mono<Delivery> find(@PathVariable String id) {
    return deliveryService.findById(id);
  }
}
