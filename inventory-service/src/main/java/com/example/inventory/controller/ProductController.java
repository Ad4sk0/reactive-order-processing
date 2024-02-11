package com.example.inventory.controller;

import com.example.inventory.service.ProductService;
import com.example.models.*;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

@Controller("/products")
public class ProductController {
  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @Get
  Flowable<Product> list() {
    return productService.findAll();
  }

  @Post
  @Status(HttpStatus.CREATED)
  @SingleResult
  Single<Product> save(@Valid Product product) {
    return productService.save(product);
  }

  @Put
  @SingleResult
  Single<Product> update(@Valid Product product) {
    if (product.id() == null) {
      return Single.error(new ValidationException("Product id is required"));
    }
    return productService.save(product);
  }

  @Get("/{id}")
  @SingleResult
  Maybe<Product> find(@PathVariable String id) {
    return productService.findById(id);
  }
}
