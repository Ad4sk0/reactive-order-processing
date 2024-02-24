package com.example.delivery.service;

import com.example.delivery.job.DeliveryJobStatus;
import com.example.models.Delivery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DeliveryService {

  Flux<Delivery> findAll();

  Mono<Delivery> save(Delivery delivery);

  Mono<Delivery> findById(String id);

  Mono<Integer> updateStatusAndEstimatedDeliveryTime(DeliveryJobStatus deliveryJob);

  Mono<Integer> completeDelivery(DeliveryJobStatus deliveryJobStatus);
}
