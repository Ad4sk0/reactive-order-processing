package com.example.order.controller;

import com.example.models.DeliveryInfo;
import com.example.models.Order;
import com.example.models.OrderItem;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller("/orders")
public class OrdersController {

    private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Get
    public List<Order> getOrders() {
        logger.info("getOrders() called");

        Order order1 =
                Order.builder()
                        .id(1L)
                        .items(List.of(new OrderItem(1L, "Product1", 1), new OrderItem(2L, "Product2", 3)))
                        .deliveryInfo(new DeliveryInfo("Street1", "City1"))
                        .build();

        Order order2 =
                Order.builder()
                        .id(2L)
                        .items(List.of(new OrderItem(2L, "Product2", 1), new OrderItem(3L, "Product3", 1)))
                        .deliveryInfo(new DeliveryInfo("Street2", "City2"))
                        .build();

        return List.of(order1, order2);
    }
}
