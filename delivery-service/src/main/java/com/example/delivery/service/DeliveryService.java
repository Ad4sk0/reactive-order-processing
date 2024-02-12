package com.example.delivery.service;

import com.example.models.Delivery;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public interface DeliveryService {

  Flowable<Delivery> findAll();

  Single<Delivery> save(Delivery delivery);

  Maybe<Delivery> findById(String id);
}
