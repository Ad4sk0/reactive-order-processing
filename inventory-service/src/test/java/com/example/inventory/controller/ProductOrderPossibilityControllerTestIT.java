package com.example.inventory.controller;

import static org.hamcrest.Matchers.*;

import com.example.inventory.AbstractContainersTest;
import com.example.models.Product;
import com.example.models.ProductOrder;
import com.example.models.ProductStatus;
import com.example.models.ProductType;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import org.junit.jupiter.api.Test;
import tests.TestsUtils;

class ProductOrderPossibilityControllerTestIT extends AbstractContainersTest {

  final String ENDPOINT = "/product-order-possibility";
  final String PRODUCTS_ENDPOINT = "/products";

  @Test
  void shouldProductOrderBePossible(RequestSpecification spec) {
    Product product =
        new Product("test-product-order-possibility-1", ProductType.PIZZA, new ProductStatus(3));
    Product createdProduct = createProduct(spec, product);
    List<ProductOrder> productOrders = List.of(new ProductOrder(null, createdProduct.id(), 1));

    spec.given()
        .contentType("application/json")
        .body(productOrders)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(200)
        .body("isPossible", equalTo(true));
  }

  @Test
  void shouldProductOrderBeNotPossibleIfIdDoesNotExist(RequestSpecification spec) {
    List<ProductOrder> productOrders =
        List.of(new ProductOrder(null, TestsUtils.createObjectId("1"), 1));

    spec.given()
        .contentType("application/json")
        .body(productOrders)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(200)
        .body("isPossible", equalTo(false));
  }

  @Test
  void shouldProductOrderBeNotPossibleIfProductHasNotEnoughQuantity(RequestSpecification spec) {
    Product product =
        new Product("test-product-order-possibility-2", ProductType.PIZZA, new ProductStatus(3));
    Product createdProduct = createProduct(spec, product);
    List<ProductOrder> productOrders = List.of(new ProductOrder(null, createdProduct.id(), 4));

    spec.given()
        .contentType("application/json")
        .body(productOrders)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(200)
        .body("isPossible", equalTo(false));
  }

  @Test
  void shouldProductOrderBeNotPossibleIfRequestContainsDuplicatedIds(RequestSpecification spec) {
    List<ProductOrder> productOrders =
        List.of(
            new ProductOrder(null, TestsUtils.createObjectId("1"), 1),
            new ProductOrder(null, TestsUtils.createObjectId("1"), 1));

    spec.given()
        .contentType("application/json")
        .body(productOrders)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(200)
        .body("isPossible", equalTo(false));
  }

  private Product createProduct(RequestSpecification spec, Product product) {
    return spec.given()
        .contentType("application/json")
        .body(product)
        .post(PRODUCTS_ENDPOINT)
        .as(Product.class);
  }
}
