package com.example.order.client;

import com.example.models.Delivery;
import com.example.models.DeliveryCancellation;
import com.example.models.DeliveryPossibility;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Mono;

@Client(id = "delivery")
public interface DeliveryClient {

  @Get("/delivery-possibility")
  Mono<DeliveryPossibility> checkDeliveryPossibility(
      @QueryValue String city, @QueryValue String street);

  @Post("/deliveries")
  Mono<Delivery> createDelivery(@Body Delivery delivery);

  @Post("/delivery-cancellations")
  Mono<DeliveryCancellation> createDeliveryCancellation(
      @Body DeliveryCancellation deliveryCancellation);
}
