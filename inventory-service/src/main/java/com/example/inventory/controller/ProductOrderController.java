package com.example.inventory.controller;

import com.example.inventory.service.ProductOrderService;
import com.example.models.*;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller("/product-orders")
public class ProductOrderController {
  private final ProductOrderService productOrderService;

  public ProductOrderController(ProductOrderService productOrderService) {
    this.productOrderService = productOrderService;
  }

  @Get
  @SingleResult
  Mono<List<ProductOrder>> list() {
    return productOrderService.findAll().collectList();
  }

  @Post
  @Status(HttpStatus.CREATED)
  @SingleResult
  Mono<List<ProductOrder>> save(@Valid @Body @NotEmpty List<ProductOrder> productOrders) {
    return productOrderService.saveAll(productOrders).collectList();
  }

  @Get("/{id}")
  @SingleResult
  Mono<ProductOrder> find(@PathVariable String id) {
    return productOrderService.findById(id);
  }
}
