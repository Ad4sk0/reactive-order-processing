package com.example.inventory.service;

import com.example.inventory.mapper.ProductOrderMapper;
import com.example.inventory.repository.ProductOrderRepository;
import com.example.models.Product;
import com.example.models.ProductOrder;
import com.example.models.ProductStatus;
import io.micronaut.transaction.annotation.Transactional;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Singleton;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

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
  public Flowable<ProductOrder> findAll() {
    return Flowable.fromPublisher(productOrderRepository.findAll()).map(ProductOrderMapper::toDTO);
  }

  @Override
  public Single<ProductOrder> save(ProductOrder productOrder) {

    Single<Product> productSingle =
        productService
            .findById(productOrder.productId())
            .switchIfEmpty(
                Single.error(
                    new ValidationException(
                        String.format(
                            "Product with id %s does not exist", productOrder.productId()))));

    return productSingle.flatMap(
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
  public Maybe<ProductOrder> findById(@NotNull String id) {
    return Maybe.fromPublisher(productOrderRepository.findById(new ObjectId(id)))
        .map(ProductOrderMapper::toDTO);
  }

  @Transactional
  Single<ProductOrder> processProductOrder(Product product, ProductOrder productOrder) {
    Single<Product> productSingle = updateProductStatus(product, productOrder);
    Single<ProductOrder> productOrderSingle = persist(productOrder);

    return Single.zip(productSingle, productOrderSingle, (status, order) -> order);
  }

  private Single<Product> updateProductStatus(Product product, ProductOrder productOrder) {
    ProductStatus updatedProductStatus =
        new ProductStatus(product.status().quantity() - productOrder.quantity());
    return productService.save(product.withStatus(updatedProductStatus));
  }

  private Single<ProductOrder> persist(ProductOrder productOrder) {
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
}
