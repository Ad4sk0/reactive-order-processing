package com.example.delivery.controller;

import com.example.delivery.service.DeliveryService;
import com.example.models.Delivery;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import jakarta.validation.Valid;

@Controller("/deliveries")
public class DeliveryController {

  private final DeliveryService deliveryService;

  public DeliveryController(DeliveryService deliveryService) {
    this.deliveryService = deliveryService;
  }

  @Get
  Flowable<Delivery> list() {
    return deliveryService.findAll();
  }

  @Post
  @Status(HttpStatus.CREATED)
  @SingleResult
  Single<Delivery> save(@Valid @Body Delivery delivery) {
    return deliveryService.save(delivery);
  }

  @Put
  @SingleResult
  Single<Delivery> update(@Valid @Body Delivery delivery) {
    return deliveryService.save(delivery);
  }

  @Get("/{id}")
  @SingleResult
  Maybe<Delivery> find(@PathVariable String id) {
    return deliveryService.findById(id);
  }
}
