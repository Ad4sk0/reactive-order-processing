package com.example.inventory.service;

import com.example.models.ProductStatus;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public interface ProductStatusService {

  Flowable<ProductStatus> findAll();

  Single<ProductStatus> save(ProductStatus product);

  Maybe<ProductStatus> findById(String id);
}
