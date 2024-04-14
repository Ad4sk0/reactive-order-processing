package com.example.delivery.event;

public record DeliveryCanceledEvent(String deliveryId) implements DeliveryEvent {}
