package com.example.models;

import lombok.Builder;

import java.util.List;


@Builder
public class Order {
    private Long id;
    private List<OrderItem> items;
    private DeliveryInfo deliveryInfo;
}
