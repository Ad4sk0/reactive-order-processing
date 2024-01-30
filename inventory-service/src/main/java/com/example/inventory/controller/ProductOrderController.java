package com.example.inventory.controller;

import com.example.inventory.service.ProductOrderService;
import com.example.models.*;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import jakarta.validation.Valid;

@Controller("/product-orders")
public class ProductOrderController {
  private final ProductOrderService productOrderService;

  public ProductOrderController(ProductOrderService productOrderService) {
    this.productOrderService = productOrderService;
  }

  @Get
  Flowable<ProductOrder> list() {
    return productOrderService.findAll();
  }

  @Post
  @Status(HttpStatus.CREATED)
  @SingleResult
  Single<ProductOrder> save(@Valid ProductOrder productOrder) {
    return productOrderService.save(productOrder);
  }

  @Get("/{id}")
  @SingleResult
  Maybe<ProductOrder> find(@PathVariable String id) {
    return productOrderService.findById(id);
  }
}
