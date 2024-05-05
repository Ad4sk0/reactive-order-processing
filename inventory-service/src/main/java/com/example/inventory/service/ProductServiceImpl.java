package com.example.inventory.service;

import com.example.inventory.entity.ProductEntity;
import com.example.inventory.mapper.ProductMapper;
import com.example.inventory.repository.ProductRepository;
import com.example.models.Product;
import com.example.models.ProductStatus;
import jakarta.inject.Singleton;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Singleton
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  public ProductServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public Flux<Product> findAll() {
    return productRepository.findAll().map(ProductMapper::toDTO);
  }

  @Override
  public Mono<Product> save(Product product) {
    if (product.id() == null) {
      return createProduct(product);
    } else {
      return updateProduct(product);
    }
  }

  @Override
  public Flux<Product> updateAll(List<Product> products) {
    return updateProducts(products);
  }

  private Mono<Product> createProduct(Product product) {
    Product productToSave;
    if (product.status() == null) {
      productToSave = product.withStatus(new ProductStatus(0));
    } else {
      productToSave = product;
    }

    return productRepository
        .findByName(product.name())
        .flatMap(
            existingProduct ->
                Mono.error(
                    new ValidationException(
                        "Product with name " + product.name() + " already exists")))
        .switchIfEmpty(productRepository.save(ProductMapper.toEntity(productToSave)))
        .cast(ProductEntity.class)
        .map(ProductMapper::toDTO);
  }

  private Mono<Product> updateProduct(Product product) {
    return findById(product.id())
        .switchIfEmpty(
            Mono.error(
                new ValidationException(
                    String.format("Product with id %s does not exist", product.id()))))
        .flatMap(exisitingProduct -> productRepository.update(ProductMapper.toEntity(product)))
        .map(ProductMapper::toDTO);
  }

  private Flux<Product> updateProducts(List<Product> products) {
    return findByIds(products.stream().map(Product::id).toList())
        .collectList()
        .flatMapMany(
            existingProducts -> {
              if (existingProducts.size() != products.size()) {
                return Flux.error(
                    new ValidationException(
                        "Unable to update products. Some of the products were not found"));
              }
              return Flux.fromIterable(products);
            })
        .map(ProductMapper::toEntity)
        .collectList()
        .flatMapMany(productRepository::updateAll)
        .map(ProductMapper::toDTO);
  }

  @Override
  public Mono<Product> findById(@NotNull String id) {
    return productRepository.findById(new ObjectId(id)).map(ProductMapper::toDTO);
  }

  @Override
  public Flux<Product> findByIds(List<String> ids) {
    List<ObjectId> objectIds = ids.stream().map(ObjectId::new).toList();
    return productRepository
        .findByIds(objectIds)
        .collectList()
        .flatMapMany(
            list -> {
              if (list.size() != ids.size()) {
                return Flux.error(new ValidationException("Unable to find some of the products"));
              }
              return Flux.fromIterable(list);
            })
        .map(ProductMapper::toDTO);
  }
}
