package com.example.delivery.service;

import com.example.delivery.job.DeliveryJobStatus;
import com.example.models.Delivery;
import com.example.models.DeliveryCancellation;
import com.example.models.DeliveryInfo;
import com.example.models.DeliveryPossibility;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DeliveryService {

  Flux<Delivery> findAll();

  Mono<Delivery> save(Delivery delivery);

  Mono<Delivery> findById(String id);

  Mono<Integer> updateStatusAndEstimatedDeliveryTime(DeliveryJobStatus deliveryJob);

  Mono<Integer> completeDelivery(DeliveryJobStatus deliveryJobStatus);

  Mono<DeliveryPossibility> isDeliveryPossible(@NotNull @Valid DeliveryInfo deliveryInfo);

  Mono<DeliveryCancellation> cancelDelivery(DeliveryCancellation deliveryCancellation);
}
