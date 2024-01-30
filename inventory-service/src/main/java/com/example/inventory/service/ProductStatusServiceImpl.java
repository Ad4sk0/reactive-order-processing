package com.example.inventory.service;

import com.example.inventory.mapper.ProductStatusMapper;
import com.example.inventory.repository.ProductStatusRepository;
import com.example.models.ProductStatus;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import jakarta.inject.Singleton;

@Singleton
public class ProductStatusServiceImpl implements ProductStatusService {

  private final ProductStatusRepository productStatusRepository;

  public ProductStatusServiceImpl(ProductStatusRepository productStatusRepository) {
    this.productStatusRepository = productStatusRepository;
  }

  @Override
  public Flowable<ProductStatus> findAll() {
    return Flowable.fromPublisher(productStatusRepository.findAll())
        .map(ProductStatusMapper::toDTO);
  }

  @Override
  public Maybe<ProductStatus> findById(String id) {
    return Maybe.fromPublisher(productStatusRepository.findById(id))
        .map(ProductStatusMapper::toDTO);
  }
}
