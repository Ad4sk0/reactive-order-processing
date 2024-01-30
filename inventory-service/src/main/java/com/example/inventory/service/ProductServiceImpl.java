package com.example.inventory.service;

import com.example.inventory.mapper.ProductMapper;
import com.example.inventory.repository.ProductRepository;
import com.example.models.Product;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

@Singleton
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  public ProductServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public Flowable<Product> findAll() {
    return Flowable.fromPublisher(productRepository.findAll()).map(ProductMapper::toDTO);
  }

  @Override
  public Single<Product> save(Product product) {
    if (product.id() == null) {
      return Single.fromPublisher(productRepository.save(ProductMapper.toEntity(product)))
          .map(ProductMapper::toDTO);
    } else {
      return Single.fromPublisher(productRepository.update(ProductMapper.toEntity(product)))
          .map(ProductMapper::toDTO);
    }
  }

  @Override
  public Maybe<Product> findById(@NotNull String id) {
    return Maybe.fromPublisher(productRepository.findById(new ObjectId(id)))
        .map(ProductMapper::toDTO);
  }
}
