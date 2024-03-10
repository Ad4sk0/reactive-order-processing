package com.example.order.service;

import com.example.models.Order;
import com.example.order.mapper.OrderMapper;
import com.example.order.repository.OrderRepository;
import jakarta.inject.Singleton;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Singleton
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;

  public OrderServiceImpl(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public Flux<Order> findAll() {
    return orderRepository.findAll().map(OrderMapper::toDTO);
  }

  @Override
  public Mono<Order> save(Order order) {
    if (order.getId() == null) {
      return orderRepository.save(OrderMapper.toEntity(order)).map(OrderMapper::toDTO);
    } else {
      return orderRepository.update(OrderMapper.toEntity(order)).map(OrderMapper::toDTO);
    }
  }

  @Override
  public Mono<Order> findById(String id) {
    return orderRepository.findById(new ObjectId(id)).map(OrderMapper::toDTO);
  }
}
