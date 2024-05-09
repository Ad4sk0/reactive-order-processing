package com.example.inventory.controller;

import com.example.inventory.service.ProductService;
import com.example.models.*;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller("/products")
public class ProductController {
  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @Get
  @SingleResult
  Mono<List<Product>> list(@Nullable @QueryValue List<String> ids) {
    if (ids != null && !ids.isEmpty()) {
      return productService.findByIds(ids).collectList();
    }
    return productService.findAll().collectList();
  }

  @Post
  @Status(HttpStatus.CREATED)
  @SingleResult
  Mono<Product> save(@Valid @Body Product product) {
    return productService.save(product);
  }

  @Put
  @SingleResult
  Mono<Product> update(@Valid @Body Product product) {
    if (product.id() == null) {
      return Mono.error(new ValidationException("Product id is required"));
    }
    return productService.save(product);
  }

  @Get("/{id}")
  @SingleResult
  Mono<Product> find(@PathVariable String id) {
    return productService.findById(id);
  }
}
