package com.example.inventory.controller;

import com.example.inventory.service.ProductOrderService;
import com.example.models.*;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import reactor.core.publisher.Mono;

@Controller("/product-order-cancellations")
public class ProductOrderCancellationController {
  private final ProductOrderService productOrderService;

  public ProductOrderCancellationController(ProductOrderService productOrderService) {
    this.productOrderService = productOrderService;
  }

  @Post
  @Status(HttpStatus.CREATED)
  @SingleResult
  Mono<List<ProductOrderCancellation>> save(
      @Valid @Body @NotEmpty List<ProductOrderCancellation> cancellations) {
    return productOrderService.cancelAll(cancellations).collectList();
  }
}
