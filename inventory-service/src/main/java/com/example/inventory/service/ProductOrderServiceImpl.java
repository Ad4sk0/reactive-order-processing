package com.example.inventory.service;

import com.example.inventory.mapper.ProductOrderMapper;
import com.example.inventory.repository.ProductOrderRepository;
import com.example.models.ProductOrder;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

@Singleton
public class ProductOrderServiceImpl implements ProductOrderService {

  private final ProductOrderRepository productOrderRepository;

  public ProductOrderServiceImpl(ProductOrderRepository productOrderRepository) {
    this.productOrderRepository = productOrderRepository;
  }

  @Override
  public Flowable<ProductOrder> findAll() {
    return Flowable.fromPublisher(productOrderRepository.findAll()).map(ProductOrderMapper::toDTO);
  }

  @Override
  public Single<ProductOrder> save(ProductOrder productOrder) {
    if (productOrder.id() == null) {
      return Single.fromPublisher(
              productOrderRepository.save(ProductOrderMapper.toEntity(productOrder)))
          .map(ProductOrderMapper::toDTO);
    } else {
      return Single.fromPublisher(
              productOrderRepository.update(ProductOrderMapper.toEntity(productOrder)))
          .map(ProductOrderMapper::toDTO);
    }
  }

  @Override
  public Maybe<ProductOrder> findById(@NotNull String id) {
    return Maybe.fromPublisher(productOrderRepository.findById(new ObjectId(id)))
        .map(ProductOrderMapper::toDTO);
  }
}
