package com.example.order.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller("/orders")
public class OrdersController {

    private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Get
    public List<String> getOrders() {
        logger.info("getOrders() called");
        return List.of("order1", "order2");
    }
}
