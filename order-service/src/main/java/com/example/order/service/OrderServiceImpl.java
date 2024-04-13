package com.example.order.service;

import com.example.models.*;
import com.example.order.client.DeliveryClient;
import com.example.order.client.InventoryClient;
import com.example.order.client.exception.InventoryClientException;
import com.example.order.entity.OrderEntity;
import com.example.order.mapper.OrderMapper;
import com.example.order.repository.OrderRepository;
import io.micronaut.data.connection.exceptions.ConnectionException;
import jakarta.inject.Singleton;
import jakarta.validation.ValidationException;
import java.util.List;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Singleton
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final InventoryClient inventoryClient;
  private final DeliveryClient deliveryClient;
  private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);

  public OrderServiceImpl(
      OrderRepository orderRepository,
      InventoryClient inventoryClient,
      DeliveryClient deliveryClient) {
    this.orderRepository = orderRepository;
    this.inventoryClient = inventoryClient;
    this.deliveryClient = deliveryClient;
  }

  @Override
  public Flux<Order> findAll() {
    return orderRepository.findAll().map(OrderMapper::toDTO);
  }

  @Override
  public Mono<Order> save(Order order) {

    List<ProductOrder> productOrdersDTOs = createProductOrdersDTOs(order);

    Mono<ProductOrderPossibility> productOrderPossibilityMono =
        checkIfProductsAreAvailable(productOrdersDTOs);
    Mono<DeliveryPossibility> checkDeliveryPossibilityMono = checkIfDeliveryIsPossible(order);

    return Mono.zip(productOrderPossibilityMono, checkDeliveryPossibilityMono)
        .flatMapMany(__ -> proceedOrder(order, productOrdersDTOs))
        .map(OrderMapper::toDTO)
        .single();
  }

  private Mono<OrderEntity> proceedOrder(Order order, List<ProductOrder> productOrdersDTOs) {
    return orderRepository
        .save(OrderMapper.toEntity(order))
        .flatMap(
            orderEntity -> createProductsOrderAndDelivery(order, productOrdersDTOs, orderEntity));
  }

  private Mono<OrderEntity> createProductsOrderAndDelivery(
      Order order, List<ProductOrder> productOrdersDTOs, OrderEntity orderEntity) {
    Delivery deliveryBody = createNewDeliveryDto(order, orderEntity);
    Flux<ProductOrder> productOrderFlux = inventoryClient.createProductOrder(productOrdersDTOs);
    Mono<Delivery> deliveryMono = deliveryClient.createDelivery(deliveryBody);
    return Flux.zip(productOrderFlux, deliveryMono)
        .onErrorResume(
            InventoryClientException.class,
            throwable -> {
              LOG.error("Failed to create product order");
              return Mono.error(new IllegalStateException("Unable to create product order"));
            })
        .flatMap(tuple2 -> updateDeliveryId(orderEntity, tuple2.getT2()))
        .single();
  }

  private Mono<OrderEntity> updateDeliveryId(OrderEntity orderEntity, Delivery delivery) {
    orderEntity.setDeliveryId(new ObjectId(delivery.id()));
    return orderRepository.save(orderEntity);
  }

  private Mono<ProductOrderPossibility> checkIfProductsAreAvailable(
      List<ProductOrder> productOrders) {

    return inventoryClient
        .getProductOrderPossibility(productOrders)
        .doOnError(
            throwable -> {
              LOG.info("Error checking product order possibility: {}", throwable.getMessage());
              throw new ConnectionException(
                  "Unable to check product order possibility in inventory service");
            })
        .map(
            productOrderPossibility -> {
              if (!productOrderPossibility.isPossible()) {
                throw new ValidationException("Product order not possible");
              }
              return productOrderPossibility;
            });
  }

  private Mono<DeliveryPossibility> checkIfDeliveryIsPossible(Order order) {
    return deliveryClient
        .checkDeliveryPossibility(order.getDeliveryInfo().city(), order.getDeliveryInfo().street())
        .doOnError(
            throwable -> {
              LOG.info("Error checking delivery possibility: {}", throwable.getMessage());
              throw new ConnectionException(
                  "Unable to check delivery possibility in delivery service");
            })
        .map(
            deliveryPossibility -> {
              if (!deliveryPossibility.isDeliveryPossible()) {
                throw new ValidationException("Delivery not possible");
              }
              return deliveryPossibility;
            });
  }

  @Override
  public Mono<Order> findById(String id) {
    return orderRepository.findById(new ObjectId(id)).map(OrderMapper::toDTO);
  }

  private List<ProductOrder> createProductOrdersDTOs(Order order) {
    return order.getItems().stream()
        .map(orderItem -> new ProductOrder(null, orderItem.productId(), orderItem.quantity()))
        .toList();
  }

  private Delivery createNewDeliveryDto(Order order, OrderEntity orderEntity) {
    return new Delivery(
        null, orderEntity.getId().toString(), order.getDeliveryInfo(), null, null, null, null);
  }
}
