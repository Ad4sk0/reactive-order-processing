package com.example.inventory.service;

import com.example.inventory.mapper.ProductOrderMapper;
import com.example.inventory.repository.ProductOrderRepository;
import com.example.models.*;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Function;
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
    return saveProducts(
            Collections.singletonList(productOrder),
            details ->
                createErrorMessageFromProductOrderPossibilityDetails(
                    details, productOrder.productId()))
        .single();
  }

  @Override
  public Flux<ProductOrder> saveAll(List<ProductOrder> productOrders) {
    return saveProducts(
            productOrders,
            details -> createErrorMessageFromProductOrderPossibilityDetails(details, null));
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

  private Flux<ProductOrder> saveProducts(
      List<ProductOrder> productOrders,
      Function<ProductOrderPossibilityDetails, String> errorMessageProvider) {

    Mono<ProductOrderPossibility> isProductOrderPossibleMono =
        isProductOrderPossible(productOrders);

    return isProductOrderPossibleMono.flatMapMany(
        orderPossibility -> {
          if (!orderPossibility.isPossible()) {
            throw new ValidationException(errorMessageProvider.apply(orderPossibility.details()));
          }
          return findProductsForAllProductOrders(productOrders)
              .collectList()
              .flatMapMany(products -> processProductOrders(products, productOrders));
        });
  }

  @Transactional
  Flux<ProductOrder> processProductOrders(
      List<Product> products, List<ProductOrder> productOrders) {
    Flux<Product> updateProductStatusesFlux = updateProductStatuses(products, productOrders);
    Flux<ProductOrder> persistProductOrdersFlux = persist(productOrders);
    return Flux.zip(updateProductStatusesFlux, persistProductOrdersFlux, (product, order) -> order);
  }

  private Flux<Product> updateProductStatuses(
      List<Product> products, List<ProductOrder> productOrders) {
    Map<String, Product> productIdToProductMap =
        products.stream().collect(Collectors.toMap(Product::id, product -> product));

    List<Product> updatedProducts = new ArrayList<>();
    for (ProductOrder productOrder : productOrders) {
      if (!productIdToProductMap.containsKey(productOrder.productId())) {
        throw new IllegalStateException(
            "Products list does not contain product order with id " + productOrder.productId());
      }
      Product product = productIdToProductMap.get(productOrder.productId());
      ProductStatus updatedProductStatus =
          new ProductStatus(product.status().quantity() - productOrder.quantity());
      updatedProducts.add(product.withStatus(updatedProductStatus));
    }
    return productService.updateAll(updatedProducts);
  }

  private Flux<ProductOrder> persist(List<ProductOrder> productOrders) {
    if (productOrders.stream().anyMatch(productOrder -> productOrder.id() != null)) {
      throw new UnsupportedOperationException("Update of product order is not supported");
    }
    return productOrderRepository
        .saveAll(productOrders.stream().map(ProductOrderMapper::toEntity).toList())
        .map(ProductOrderMapper::toDTO);
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

  private String createErrorMessageFromProductOrderPossibilityDetails(
          ProductOrderPossibilityDetails details, String productId) {
    if (details == null || details.reason() == null) {
      return "Product order is not possible";
    }

    if (productId != null) {
      return switch (details.reason()) {
        case SOME_PRODUCTS_DO_NOT_EXIST -> "Product with id " + productId + " does not exist";
        case NOT_ENOUGH_QUANTITY ->
                "Product with id " + productId + " does not have enough quantity";
        default -> "Unexpected problem";
      };
    }

    return switch (details.reason()) {
      case DUPLICATE_IN_PRODUCT_ORDER_LIST -> "Duplicate product in product order list found";
      case SOME_PRODUCTS_DO_NOT_EXIST -> "Some of the ordered products do not exist";
      case NOT_ENOUGH_QUANTITY -> "Some of the ordered products do not have enough quantity";
    };
  }
}
