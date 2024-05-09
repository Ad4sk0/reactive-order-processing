package com.example.inventory.controller;

import static org.hamcrest.Matchers.*;

import com.example.inventory.AbstractContainersTest;
import com.example.models.Product;
import com.example.models.ProductStatus;
import com.example.models.ProductType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

class ProductControllerTestIT extends AbstractContainersTest {

  final String ENDPOINT = "/products";

  @Test
  void shouldCreateDefaultProductStatusWhenNotSpecified(RequestSpecification spec) {
    Product product = new Product(null, "test-product-1", ProductType.PIZZA, null);
    spec.given()
        .contentType("application/json")
        .body(product)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(201)
        .body("status.quantity", equalTo(0));
  }

  @Test
  void shouldCreateProductStatusWhenSpecified(RequestSpecification spec) {
    Product product = new Product(null, "test-product-2", ProductType.PIZZA, new ProductStatus(10));
    spec.given()
        .contentType("application/json")
        .body(product)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(201)
        .body("status.quantity", equalTo(10));
  }

  @Test
  void shouldNotCreateProductWhenNameExists(RequestSpecification spec) {
    Product product = new Product(null, "test-product-3", ProductType.PIZZA, null);
    spec.given()
        .contentType("application/json")
        .body(product)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(201);

    Product product2 = new Product(null, "test-product-3", ProductType.PIZZA, null);
    spec.given()
        .contentType("application/json")
        .body(product2)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(400);
  }

  @Test
  void shouldNotCreateProductWhenNameIsEmpty(RequestSpecification spec) {
    Product product = new Product(null, null, ProductType.PIZZA, null);
    spec.given()
        .contentType("application/json")
        .body(product)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(400);
  }

  @Test
  void shouldNotCreateProductIfStatusQuantityIsNegative(RequestSpecification spec) {
    Product product = new Product(null, "test-product-4", ProductType.PIZZA, new ProductStatus(-2));
    spec.given()
        .contentType("application/json")
        .body(product)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(400);
  }

  @Test
  void shouldUpdateProductIfStatusQuantityIsPositive(RequestSpecification spec) {
    Product product = new Product(null, "test-product-4", ProductType.PIZZA, new ProductStatus(10));
    Product createdProduct =
        spec.given().contentType("application/json").body(product).post(ENDPOINT).as(Product.class);

    Product updatedProduct = createdProduct.withStatus(new ProductStatus(20));

    spec.given()
        .contentType("application/json")
        .body(updatedProduct)
        .when()
        .put(ENDPOINT)
        .then()
        .statusCode(200)
        .body("status.quantity", equalTo(20));
  }

  @Test
  void shouldNotUpdateProductIfStatusQuantityIsNegative(RequestSpecification spec) {
    Product product = new Product(null, "test-product-5", ProductType.PIZZA, new ProductStatus(10));
    Product createdProduct =
        spec.given().contentType("application/json").body(product).post(ENDPOINT).as(Product.class);

    Product updatedProduct = createdProduct.withStatus(new ProductStatus(-20));

    spec.given()
        .contentType("application/json")
        .body(updatedProduct)
        .when()
        .put(ENDPOINT)
        .then()
        .statusCode(400);
  }

  @Test
  void shouldCreateAndRetrieveProduct(RequestSpecification spec) {
    Product product = new Product(null, "test-product-6", ProductType.PIZZA, null);
    Product createdProduct =
        spec.given().contentType("application/json").body(product).post(ENDPOINT).as(Product.class);

    spec.given()
        .contentType("application/json")
        .when()
        .get(ENDPOINT + "/" + createdProduct.id())
        .then()
        .statusCode(200)
        .body("name", equalTo("test-product-6"));
  }

  @Test
  void shouldListProducts(RequestSpecification spec) {
    Product product = new Product(null, "test-product-7", ProductType.PIZZA, null);
    Product product2 = new Product(null, "test-product-8", ProductType.PIZZA, null);
    spec.given()
        .contentType("application/json")
        .body(product)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(201);

    spec.given()
        .contentType("application/json")
        .body(product2)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(201);

    spec.given()
        .contentType("application/json")
        .when()
        .get(ENDPOINT)
        .then()
        .statusCode(200)
        .body("$", is(not(empty())));
  }
}
