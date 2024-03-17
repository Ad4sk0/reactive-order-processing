package com.example.inventory.service;

import com.example.inventory.mapper.ProductOrderMapper;
import com.example.inventory.repository.ProductOrderRepository;
import com.example.models.*;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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

  @Override
  public Mono<ProductOrderPossibility> isProductOrderPossible(List<ProductOrder> productOrders) {
    if (containsDuplicateProductIds(productOrders)) {
      return createOrderPossibilityMono(
          false, ProductOrderPossibilityErrorReason.DUPLICATE_IN_PRODUCT_ORDER_LIST);
    }
    return findProductsForAllProductOrders(productOrders)
        .collectList()
        .flatMap(
            productList -> {
              if (containsProductWithNotEnoughQuantity(productList, productOrders)) {
                return createOrderPossibilityMono(
                    false, ProductOrderPossibilityErrorReason.NOT_ENOUGH_QUANTITY);
              }
              return createOrderPossibilityMono(true, null);
            })
        .onErrorResume(
            ValidationException.class,
            throwable ->
                createOrderPossibilityMono(
                    false, ProductOrderPossibilityErrorReason.SOME_PRODUCTS_DO_NOT_EXIST));
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

  private Flux<Product> findProductsForAllProductOrders(List<ProductOrder> products) {
    List<String> productIds = products.stream().map(ProductOrder::productId).toList();
    return productService.findByIds(productIds);
  }

  private boolean containsDuplicateProductIds(List<ProductOrder> products) {
    Set<String> productIdsSet =
        products.stream().map(ProductOrder::productId).collect(Collectors.toSet());
    return productIdsSet.size() != products.size();
  }

  private boolean containsProductWithNotEnoughQuantity(
      List<Product> productList, List<ProductOrder> productOrders) {
    Map<String, ProductStatus> productsWithNotEnoughQuantity =
        findProductsWithNotEnoughQuantity(productList, createOrderQuantitiesMap(productOrders));
    return !productsWithNotEnoughQuantity.isEmpty();
  }

  private Map<String, ProductStatus> findProductsWithNotEnoughQuantity(
      List<Product> products, Map<String, Integer> orderQuantities) {
    return products.stream()
        .filter(product -> product.status().quantity() < orderQuantities.get(product.id()))
        .collect(Collectors.toMap(Product::id, Product::status));
  }

  private Map<String, Integer> createOrderQuantitiesMap(List<ProductOrder> products) {
    return products.stream()
        .collect(Collectors.toMap(ProductOrder::productId, ProductOrder::quantity, (a, b) -> a));
  }

  private Mono<ProductOrderPossibility> createOrderPossibilityMono(
      boolean isPossible, ProductOrderPossibilityErrorReason reason) {
    return Mono.just(
        new ProductOrderPossibility(isPossible, new ProductOrderPossibilityDetails(reason)));
  }
}
