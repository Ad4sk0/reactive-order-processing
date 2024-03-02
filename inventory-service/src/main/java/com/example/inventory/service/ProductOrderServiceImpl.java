package com.example.inventory.service;

import com.example.inventory.mapper.ProductOrderMapper;
import com.example.inventory.repository.ProductOrderRepository;
import com.example.models.Product;
import com.example.models.ProductOrder;
import com.example.models.ProductStatus;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Singleton
public class ProductOrderServiceImpl implements ProductOrderService {

  private final ProductOrderRepository productOrderRepository;
  private final ProductService productService;

  public ProductOrderServiceImpl(
      ProductOrderRepository productOrderRepository, ProductService productService) {
    this.productOrderRepository = productOrderRepository;
    this.productService = productService;
  }

  @Override
  public Flux<ProductOrder> findAll() {
    return productOrderRepository.findAll().map(ProductOrderMapper::toDTO);
  }

  @Override
  public Mono<ProductOrder> save(ProductOrder productOrder) {

    Mono<Product> productMono =
        productService
            .findById(productOrder.productId())
            .switchIfEmpty(
                Mono.error(
                    new ValidationException(
                        String.format(
                            "Product with id %s does not exist", productOrder.productId()))));

    return productMono.flatMap(
        product -> {
          if (product.status().quantity() < productOrder.quantity()) {
            throw new ValidationException(
                String.format(
                    "Product with id %s does not have enough quantity", productOrder.productId()));
          }
          return processProductOrder(product, productOrder);
        });
  }

  @Override
  public Mono<ProductOrder> findById(@NotNull String id) {
    return productOrderRepository.findById(new ObjectId(id)).map(ProductOrderMapper::toDTO);
  }

  @Transactional
  Mono<ProductOrder> processProductOrder(Product product, ProductOrder productOrder) {
    Mono<Product> productMono = updateProductStatus(product, productOrder);
    Mono<ProductOrder> productOrderMono = persist(productOrder);

    return Mono.zip(productMono, productOrderMono, (status, order) -> order);
  }

  private Mono<Product> updateProductStatus(Product product, ProductOrder productOrder) {
    ProductStatus updatedProductStatus =
        new ProductStatus(product.status().quantity() - productOrder.quantity());
    return productService.save(product.withStatus(updatedProductStatus));
  }

  private Mono<ProductOrder> persist(ProductOrder productOrder) {
    if (productOrder.id() == null) {
      return productOrderRepository
          .save(ProductOrderMapper.toEntity(productOrder))
          .map(ProductOrderMapper::toDTO);
    } else {
      return productOrderRepository
          .update(ProductOrderMapper.toEntity(productOrder))
          .map(ProductOrderMapper::toDTO);
    }
  }
}
