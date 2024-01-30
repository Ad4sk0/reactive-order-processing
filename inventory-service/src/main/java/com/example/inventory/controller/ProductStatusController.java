package com.example.inventory.controller;

import com.example.inventory.service.ProductStatusService;
import com.example.models.*;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.annotation.*;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

@Controller("/product-statuses")
public class ProductStatusController {
  private final ProductStatusService productStatusService;

  public ProductStatusController(ProductStatusService productStatusService) {
    this.productStatusService = productStatusService;
  }

  @Get
  Flowable<ProductStatus> list() {
    return productStatusService.findAll();
  }

  @Get("/{id}")
  @SingleResult
  Maybe<ProductStatus> find(@PathVariable String id) {
    return productStatusService.findById(id);
  }
}
