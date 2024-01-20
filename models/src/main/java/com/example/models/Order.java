package com.example.models;

import java.util.List;


public class Order {
    private Long id;
    private List<OrderItem> items;
    private DeliveryInfo deliveryInfo;
}
