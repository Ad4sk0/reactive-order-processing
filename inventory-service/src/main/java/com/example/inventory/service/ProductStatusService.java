package com.example.inventory.service;

import com.example.models.ProductStatus;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

public interface ProductStatusService {

  Flowable<ProductStatus> findAll();

  Maybe<ProductStatus> findById(String id);
}
