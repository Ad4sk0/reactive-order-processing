package com.example.inventory.service;

import com.example.models.ProductOrder;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public interface ProductOrderService {

  Flowable<ProductOrder> findAll();

  Single<ProductOrder> save(ProductOrder product);

  Maybe<ProductOrder> findById(String id);
}
