package com.example.order.service;

import com.example.models.Order;
import com.example.order.mapper.OrderMapper;
import com.example.order.repository.OrderRepository;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Singleton;
import org.bson.types.ObjectId;

@Singleton
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;

  public OrderServiceImpl(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public Flowable<Order> findAll() {
    return Flowable.fromPublisher(orderRepository.findAll()).map(OrderMapper::toDTO);
  }

  @Override
  public Single<Order> save(Order order) {
    if (order.getId() == null) {
      return Single.fromPublisher(orderRepository.save(OrderMapper.toEntity(order)))
          .map(OrderMapper::toDTO);
    } else {
      return Single.fromPublisher(orderRepository.update(OrderMapper.toEntity(order)))
          .map(OrderMapper::toDTO);
    }
  }

  @Override
  public Maybe<Order> findById(String id) {
    return Maybe.fromPublisher(orderRepository.findById(new ObjectId(id))).map(OrderMapper::toDTO);
  }
}
