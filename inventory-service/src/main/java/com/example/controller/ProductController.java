package com.example.controller;

import com.example.models.*;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/products")
public class ProductController {
  private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

  @Get
  public Observable<Product> getProducts() {
    logger.info("getProducts() called");

    List<Ingredient> ingredients1 =
        List.of(new Ingredient(1L, "Ingredient1", true), new Ingredient(2L, "Ingredient2", true));
    Product product1 = new Product(1L, "Product1", true, ingredients1);

    List<Ingredient> ingredients2 =
        List.of(
            new Ingredient(3L, "Ingredient3", true),
            new Ingredient(4L, "Ingredient4", true),
            new Ingredient(5L, "Ingredient5", false));
    Product product2 = new Product(2L, "Product2", false, ingredients2);

    return Observable.just(product1, product2);
  }
}
