package com.example.inventory.service;

import com.example.inventory.mapper.ProductMapper;
import com.example.inventory.repository.ProductRepository;
import com.example.models.Product;
import com.example.models.ProductStatus;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Singleton;
import jakarta.validation.ValidationException;
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
      return createProduct(product);
    } else {
      return updateProduct(product);
    }
  }

  private Single<Product> createProduct(Product product) {
    Product productToSave;
    if (product.status() == null) {
      productToSave = product.withStatus(new ProductStatus(0));
    } else {
      productToSave = product;
    }

    return Maybe.fromPublisher(productRepository.findByName(product.name()))
        .isEmpty()
        .flatMap(
            isEmpty -> {
              if (isEmpty) {
                return Single.fromPublisher(
                    productRepository.save(ProductMapper.toEntity(productToSave)));
              } else {
                return Single.error(
                    new ValidationException(
                        "Product with name " + product.name() + " already exists"));
              }
            })
        .map(ProductMapper::toDTO);
  }

  private Single<Product> updateProduct(Product product) {
    return findById(product.id())
        .switchIfEmpty(
            Single.error(
                new ValidationException(
                    String.format("Product with id %s does not exist", product.id()))))
        .flatMap(
            exisitingProduct ->
                Single.fromPublisher(productRepository.update(ProductMapper.toEntity(product))))
        .map(ProductMapper::toDTO);
  }

  @Override
  public Maybe<Product> findById(@NotNull String id) {
    return Maybe.fromPublisher(productRepository.findById(new ObjectId(id)))
        .map(ProductMapper::toDTO);
  }
}
