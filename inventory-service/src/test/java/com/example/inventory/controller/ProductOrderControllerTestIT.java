package com.example.inventory.controller;

import static org.hamcrest.Matchers.*;

import com.example.inventory.AbstractContainersTest;
import com.example.models.Product;
import com.example.models.ProductOrder;
import com.example.models.ProductStatus;
import com.example.models.ProductType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

class ProductOrderControllerTestIT extends AbstractContainersTest {

  final String ENDPOINT = "/product-orders";
  final String PRODUCTS_ENDPOINT = "/products";

  @Test
  void shouldCreateProductOrder(RequestSpecification spec) {
    Product product =
        new Product(null, "test-product-order-1", ProductType.PIZZA, new ProductStatus(3));
    Product createdProduct = createProduct(spec, product);
    ProductOrder productOrder = new ProductOrder(null, createdProduct.id(), 1);

    spec.given()
        .contentType("application/json")
        .body(productOrder)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(201)
        .body(
            "productId", equalTo(createdProduct.id()),
            "quantity", equalTo(1));
  }

  @Test
  void shouldNotCreateProductOrderIfProductIdNotSpecified(RequestSpecification spec) {
    ProductOrder productOrder = new ProductOrder(null, null, 1);

    spec.given()
        .contentType("application/json")
        .body(productOrder)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(400);
  }

  @Test
  void shouldNotCreateProductOrderIfProductIdDoesNotExist(RequestSpecification spec) {
    ProductOrder productOrder = new ProductOrder(null, "323456789123456789123456", 1);

    spec.given()
        .contentType("application/json")
        .body(productOrder)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(500);
  }

  @Test
  void shouldNotCreateProductOrderIfProductQuantityIsToSmall(RequestSpecification spec) {
    Product product =
        new Product(null, "test-product-order-2", ProductType.PIZZA, new ProductStatus(3));
    Product createdProduct = createProduct(spec, product);
    ProductOrder productOrder = new ProductOrder(null, createdProduct.id(), 4);

    spec.given()
        .contentType("application/json")
        .body(productOrder)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(500);
  }

  @Test
  void shouldCreateProductOrderAndReduceProductQuantity(RequestSpecification spec) {
    Product product =
        new Product(null, "test-product-order-3", ProductType.PIZZA, new ProductStatus(3));
    Product createdProduct = createProduct(spec, product);
    ProductOrder productOrder = new ProductOrder(null, createdProduct.id(), 1);

    spec.given()
        .contentType("application/json")
        .when()
        .get(PRODUCTS_ENDPOINT + "/" + createdProduct.id())
        .then()
        .statusCode(200)
        .body("status.quantity", equalTo(3));

    spec.given().contentType("application/json").body(productOrder).post(ENDPOINT);

    spec.given()
        .contentType("application/json")
        .when()
        .get(PRODUCTS_ENDPOINT + "/" + createdProduct.id())
        .then()
        .statusCode(200)
        .body("status.quantity", equalTo(2));
  }

  private Product createProduct(RequestSpecification spec, Product product) {
    return spec.given()
        .contentType("application/json")
        .body(product)
        .post(PRODUCTS_ENDPOINT)
        .as(Product.class);
  }
}
