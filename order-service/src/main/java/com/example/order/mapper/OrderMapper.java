package com.example.order.mapper;

import com.example.models.Order;
import com.example.order.entity.OrderEntity;
import java.util.Optional;

public class OrderMapper {

  private OrderMapper() {}

  public static OrderEntity toEntity(Order order) {
    if (order == null) {
      return null;
    }

    return OrderEntity.builder()
        .orderItems(
            Optional.ofNullable(order.items())
                .map(orderItems -> orderItems.stream().map(OrderItemMapper::toEntity).toList())
                .orElse(null))
        .deliveryInfo(DeliveryInfoMapper.toEntity(order.deliveryInfo()))
        .build();
  }

  public static Order toDTO(OrderEntity orderEntity) {
    if (orderEntity == null) {
      return null;
    }

    return new Order(
        orderEntity.getId().toString(),
        Optional.ofNullable(orderEntity.getOrderItems())
            .map(orderItems -> orderItems.stream().map(OrderItemMapper::toDTO).toList())
            .orElse(null),
        DeliveryInfoMapper.toDTO(orderEntity.getDeliveryInfo()),
        orderEntity.getDeliveryId().toString(),
        orderEntity.getAuditData());
  }
}
