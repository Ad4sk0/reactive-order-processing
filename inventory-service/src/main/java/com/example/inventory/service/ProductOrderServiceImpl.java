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
  private final ProductStatusService productStatusService;

  public ProductOrderServiceImpl(
      ProductOrderRepository productOrderRepository,
      ProductService productService,
      ProductStatusService productStatusService) {
    this.productOrderRepository = productOrderRepository;
    this.productService = productService;
    this.productStatusService = productStatusService;
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

    Single<ProductStatus> statusSingle =
        productStatusService
            .findById(productOrder.productId())
            .switchIfEmpty(
                Single.error(
                    new ValidationException(
                        String.format(
                            "Product status with id %s does not exist",
                            productOrder.productId()))));

    return Single.zip(
            productSingle,
            statusSingle,
            (product, productStatus) -> {
              if (productStatus.quantity() < productOrder.quantity()) {
                throw new ValidationException(
                    String.format(
                        "Product with id %s does not have enough quantity",
                        productOrder.productId()));
              }
              return productStatus;
            })
        .flatMap(productStatus -> processProductOrder(productStatus, productOrder));
  }

  @Override
  public Maybe<ProductOrder> findById(@NotNull String id) {
    return Maybe.fromPublisher(productOrderRepository.findById(new ObjectId(id)))
        .map(ProductOrderMapper::toDTO);
  }

  @Transactional
  Single<ProductOrder> processProductOrder(ProductStatus productStatus, ProductOrder productOrder) {
    Single<ProductStatus> productStatusSingle = updateProductStatus(productStatus, productOrder);
    Single<ProductOrder> productOrderSingle = persist(productOrder);

    return Single.zip(productStatusSingle, productOrderSingle, (status, order) -> order);
  }

  private Single<ProductStatus> updateProductStatus(
      ProductStatus productStatus, ProductOrder productOrder) {
    ProductStatus updatedProductStatus =
        new ProductStatus(productStatus.id(), productStatus.quantity() - productOrder.quantity());
    return productStatusService.save(updatedProductStatus);
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
