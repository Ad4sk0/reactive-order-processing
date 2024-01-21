package com.example.order.service;

import com.example.models.Order;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public interface OrderService {

  Flowable<Order> findAll();

  Single<Order> save(Order order);

  Maybe<Order> findById(String id);
}
