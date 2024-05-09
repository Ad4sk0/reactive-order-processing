package com.example.delivery.controller;

import com.example.delivery.service.DeliveryService;
import com.example.models.DeliveryInfo;
import com.example.models.DeliveryPossibility;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import reactor.core.publisher.Mono;

@Controller("/delivery-possibility")
public class DeliveryPossibilityController {

  private final DeliveryService deliveryService;

  public DeliveryPossibilityController(DeliveryService deliveryService) {
    this.deliveryService = deliveryService;
  }

  @Get
  @SingleResult
  Mono<DeliveryPossibility> isDeliveryPossible(@QueryValue String city, @QueryValue String street) {
    return deliveryService.isDeliveryPossible(new DeliveryInfo(street, city));
  }
}
