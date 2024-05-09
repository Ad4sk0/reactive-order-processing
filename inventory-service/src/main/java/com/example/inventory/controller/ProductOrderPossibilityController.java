package com.example.inventory.controller;

import com.example.inventory.service.ProductOrderService;
import com.example.models.ProductOrder;
import com.example.models.ProductOrderPossibility;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.annotation.*;
import java.util.List;
import reactor.core.publisher.Mono;

@Controller("/product-order-possibility")
public class ProductOrderPossibilityController {

  private final ProductOrderService productOrderService;

  public ProductOrderPossibilityController(ProductOrderService productOrderService) {
    this.productOrderService = productOrderService;
  }

  @Post
  @SingleResult
  Mono<ProductOrderPossibility> isProductOrderPossible(@Body List<ProductOrder> products) {
    return productOrderService.isProductOrderPossible(products);
  }
}
