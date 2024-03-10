package com.example.order.service;

import com.example.models.Order;
import com.example.models.OrderItem;
import com.example.models.Product;
import com.example.order.client.InventoryClient;
import com.example.order.mapper.OrderMapper;
import com.example.order.repository.OrderRepository;
import jakarta.inject.Singleton;
import jakarta.validation.ValidationException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Singleton
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final InventoryClient inventoryClient;
  private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);

  public OrderServiceImpl(OrderRepository orderRepository, InventoryClient inventoryClient) {
    this.orderRepository = orderRepository;
    this.inventoryClient = inventoryClient;
  }

  @Override
  public Flux<Order> findAll() {
    return orderRepository.findAll().map(OrderMapper::toDTO);
  }

  @Override
  public Mono<Order> save(Order order) {

    Map<String, Integer> orderQuantities =
        order.getItems().stream()
            .collect(Collectors.toMap(OrderItem::productId, OrderItem::quantity, (a, b) -> a));
    Set<String> productIds = orderQuantities.keySet();

    if (order.getItems().size() != productIds.size()) {
      throw new IllegalArgumentException("Duplicate products in order items found");
    }

    Flux<Product> inventoryProductFlux =
        inventoryClient
            .fetchProducts(productIds)
            .doOnError(
                throwable -> {
                  LOG.info("Error fetching products from inventory: {}", throwable.getMessage());
                  throw new IllegalArgumentException("Unable to fetch given product ids");
                })
            .collectList()
            .flatMapMany(
                list -> {
                  if (list.size() != productIds.size()) {
                    LOG.info(
                        "Unable to find all product ids in inventory: inventory products size {} vs. order products size {}",
                        list.size(),
                        productIds.size());
                    throw new IllegalArgumentException("Unable to find given product ids");
                  }
                  return Flux.fromIterable(list);
                })
            .flatMap(
                product -> {
                  if (product.status().quantity() < orderQuantities.get(product.id())) {
                    return Mono.error(
                        new ValidationException(
                            "Not enough quantity for product: " + product.name()));
                  }
                  return Mono.just(product);
                });

    return inventoryProductFlux
        .collectList()
        .flatMapMany(inventoryProducts -> orderRepository.save(OrderMapper.toEntity(order)))
        .next()
        .map(OrderMapper::toDTO);
  }

  @Override
  public Mono<Order> findById(String id) {
    return orderRepository.findById(new ObjectId(id)).map(OrderMapper::toDTO);
  }
}
