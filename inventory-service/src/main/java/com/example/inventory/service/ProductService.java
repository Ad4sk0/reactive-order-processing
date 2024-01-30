package com.example.inventory.service;

import com.example.models.Product;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public interface ProductService {

  Flowable<Product> findAll();

  Single<Product> save(Product product);

  Maybe<Product> findById(String id);
}
