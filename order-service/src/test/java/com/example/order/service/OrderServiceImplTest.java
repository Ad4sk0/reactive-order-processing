package com.example.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tests.TestsUtils.createObjectId;

import com.example.models.*;
import com.example.order.client.DeliveryClient;
import com.example.order.client.InventoryClient;
import com.example.order.entity.OrderEntity;
import com.example.order.repository.OrderRepository;
import jakarta.validation.ValidationException;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class OrderServiceImplTest {

  OrderService orderService;

  OrderRepository orderRepository;

  InventoryClient inventoryClient;

  DeliveryClient deliveryClient;

  @BeforeEach
  void init() {
    orderRepository = createOrderRepositoryMock();
    inventoryClient = createInventoryClientMock();
    deliveryClient = createDeliveryClientMock();

    orderService = new OrderServiceImpl(orderRepository, inventoryClient, deliveryClient);
  }

  @Test
  void shouldCreateOrder() {
    List<OrderItem> orderItems = List.of(new OrderItem("1", 1), new OrderItem("2", 2));
    Order order =
        Order.builder()
            .items(orderItems)
            .deliveryInfo(new DeliveryInfo("TestStreet", "TestCity"))
            .build();

    StepVerifier.create(orderService.save(order))
        .assertNext(
            savedOrder -> {
              assertNotNull(savedOrder.getId());
              assertEquals("TestStreet", savedOrder.getDeliveryInfo().street());
              assertEquals("TestCity", savedOrder.getDeliveryInfo().city());
              assertEquals(2, savedOrder.getItems().size());
              assertEquals("1", savedOrder.getItems().getFirst().productId());
              assertEquals(1, savedOrder.getItems().getFirst().quantity());
              assertEquals("2", savedOrder.getItems().get(1).productId());
              assertEquals(2, savedOrder.getItems().get(1).quantity());
            })
        .verifyComplete();
  }

  @Test
  void shouldNotCreateOrderIfDeliveryIsNotPossible() {
    when(deliveryClient.checkDeliveryPossibility(any(), any()))
        .thenReturn(Mono.just(new DeliveryPossibility(false, null, null, null)));
    List<OrderItem> orderItems = List.of(new OrderItem("1", 1), new OrderItem("2", 2));
    Order order =
        Order.builder()
            .items(orderItems)
            .deliveryInfo(new DeliveryInfo("TestStreet", "TestCity"))
            .build();

    StepVerifier.create(orderService.save(order)).expectError(ValidationException.class).verify();
  }

  @Test
  void shouldNotCreateOrderIfProductsAreNotAvailable() {
    when(inventoryClient.getProductOrderPossibility(any()))
        .thenReturn(Mono.just(new ProductOrderPossibility(false, null)));
    List<OrderItem> orderItems = List.of(new OrderItem("1", 1), new OrderItem("2", 2));
    Order order =
        Order.builder()
            .items(orderItems)
            .deliveryInfo(new DeliveryInfo("TestStreet", "TestCity"))
            .build();

    StepVerifier.create(orderService.save(order)).expectError(ValidationException.class).verify();
  }

  private OrderRepository createOrderRepositoryMock() {
    OrderRepository orderRepositoryMock = mock(OrderRepository.class);
    when(orderRepositoryMock.save(any()))
        .thenAnswer(
            invocation -> {
              OrderEntity orderEntity = invocation.getArgument(0);
              return Mono.just(
                  OrderEntity.builder()
                      .id(new ObjectId(createObjectId("1")))
                      .deliveryId(new ObjectId(createObjectId("1")))
                      .orderItems(orderEntity.getOrderItems())
                      .deliveryInfo(orderEntity.getDeliveryInfo())
                      .build());
            });
    return orderRepositoryMock;
  }

  private InventoryClient createInventoryClientMock() {
    InventoryClient inventoryClientMock = mock(InventoryClient.class);
    when(inventoryClientMock.getProductOrderPossibility(any()))
        .thenReturn(Mono.just(new ProductOrderPossibility(true, null)));
    when(inventoryClientMock.createProductOrder(any()))
        .thenReturn(Flux.just(new ProductOrder(createObjectId("1"), createObjectId("1"), 1)));
    return inventoryClientMock;
  }

  private DeliveryClient createDeliveryClientMock() {
    DeliveryClient deliveryClientMock = mock(DeliveryClient.class);
    when(deliveryClientMock.checkDeliveryPossibility(any(), any()))
        .thenReturn(Mono.just(new DeliveryPossibility(true, null, null, null)));
    when(deliveryClientMock.createDelivery(any()))
        .thenReturn(
            Mono.just(
                new Delivery(
                    createObjectId("1"), createObjectId("1"), null, null, null, null, null)));

    return deliveryClientMock;
  }
}
