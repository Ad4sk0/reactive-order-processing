package com.example.delivery.controller;

import com.example.delivery.service.DeliveryService;
import com.example.models.*;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@Controller("/delivery-cancellations")
public class DeliveryCancellationController {
  private final DeliveryService deliveryService;

  public DeliveryCancellationController(DeliveryService deliveryService) {
    this.deliveryService = deliveryService;
  }

  @Post
  @Status(HttpStatus.CREATED)
  Mono<DeliveryCancellation> save(@Valid @Body DeliveryCancellation deliveryCancellation) {
    return deliveryService.cancelDelivery(deliveryCancellation);
  }
}
