package com.example.order.client;

import com.example.models.DeliveryPossibility;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Mono;

@Client(id = "delivery")
public interface DeliveryClient {

  @Get("/delivery-possibility")
  Mono<DeliveryPossibility> checkDeliveryPossibility(@QueryValue String city, @QueryValue String street);
}
