package com.example.order.mapper;

import com.example.models.Order;
import com.example.order.entity.OrderEntity;
import java.util.Optional;

public class OrderMapper {

  private OrderMapper() {}

  public static OrderEntity toEntity(Order order) {
    return OrderEntity.builder()
        .id(order.getId())
        .orderItems(
            Optional.ofNullable(order.getItems())
                .map(orderItems -> orderItems.stream().map(OrderItemMapper::toEntity).toList())
                .orElse(null))
        .deliveryInfo(DeliveryInfoMapper.toEntity(order.getDeliveryInfo()))
        .build();
  }

  public static Order toDTO(OrderEntity orderEntity) {
    return Order.builder()
        .id(orderEntity.getId())
        .items(
            Optional.ofNullable(orderEntity.getOrderItems())
                .map(orderItems -> orderItems.stream().map(OrderItemMapper::toDTO).toList())
                .orElse(null))
        .deliveryInfo(DeliveryInfoMapper.toDTO(orderEntity.getDeliveryInfo()))
        .build();
  }
}
