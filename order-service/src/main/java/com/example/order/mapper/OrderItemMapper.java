package com.example.order.mapper;

import com.example.models.OrderItem;
import com.example.order.entity.OrderItemEmbeddable;

public class OrderItemMapper {

  private OrderItemMapper() {}

  public static OrderItemEmbeddable toEntity(OrderItem orderItem) {
    if (orderItem == null) {
      return null;
    }
    return new OrderItemEmbeddable(
        orderItem.productId(), orderItem.productName(), orderItem.quantity());
  }

  public static OrderItem toDTO(OrderItemEmbeddable orderItemEmbeddable) {
    if (orderItemEmbeddable == null) {
      return null;
    }
    return new OrderItem(
        orderItemEmbeddable.productId(),
        orderItemEmbeddable.productName(),
        orderItemEmbeddable.quantity());
  }
}
