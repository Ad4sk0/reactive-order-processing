package com.example.inventory.controller;

import com.example.inventory.service.ProductOrderService;
import com.example.models.*;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller("/product-orders")
public class ProductOrderController {
  private final ProductOrderService productOrderService;

  public ProductOrderController(ProductOrderService productOrderService) {
    this.productOrderService = productOrderService;
  }

  @Get
  Flux<ProductOrder> list() {
    return productOrderService.findAll();
  }

  @Post
  @Status(HttpStatus.CREATED)
  @SingleResult
  Mono<ProductOrder> save(@Valid @Body ProductOrder productOrder) {
    return productOrderService.save(productOrder);
  }

  @Get("/{id}")
  @SingleResult
  Mono<ProductOrder> find(@PathVariable String id) {
    return productOrderService.findById(id);
  }
}
