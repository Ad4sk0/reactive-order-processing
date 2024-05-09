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

class ProductOrderControllerTestIT extends AbstractContainersTest {

  final String ENDPOINT = "/product-orders";
  final String PRODUCTS_ENDPOINT = "/products";

  @Test
  void shouldCreateProductOrder(RequestSpecification spec) {
    Product product = new Product("test-product-order-1", ProductType.PIZZA, new ProductStatus(3));
    Product createdProduct = createProduct(spec, product);
    List<ProductOrder> productOrders = List.of(new ProductOrder(null, createdProduct.id(), 1));

    spec.given()
        .contentType("application/json")
        .body(productOrders)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(201)
        .body(
            "productId[0]", equalTo(createdProduct.id()),
            "quantity[0]", equalTo(1));
  }

  @Test
  void shouldNotCreateProductOrderIfProductIdNotSpecified(RequestSpecification spec) {
    List<ProductOrder> productOrders = List.of(new ProductOrder(null, null, 1));

    spec.given()
        .contentType("application/json")
        .body(productOrders)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(400);
  }

  @Test
  void shouldNotCreateProductOrderIfProductIdDoesNotExist(RequestSpecification spec) {
    List<ProductOrder> productOrders =
        List.of(new ProductOrder(null, "323456789123456789123456", 1));

    spec.given()
        .contentType("application/json")
        .body(productOrders)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(400);
  }

  @Test
  void shouldNotCreateProductOrderIfProductQuantityIsToSmall(RequestSpecification spec) {
    Product product = new Product("test-product-order-2", ProductType.PIZZA, new ProductStatus(3));
    Product createdProduct = createProduct(spec, product);
    List<ProductOrder> productOrders = List.of(new ProductOrder(null, createdProduct.id(), 4));

    spec.given()
        .contentType("application/json")
        .body(productOrders)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(400);
  }

  @Test
  void shouldCreateProductOrderAndReduceProductQuantity(RequestSpecification spec) {
    Product product = new Product("test-product-order-3", ProductType.PIZZA, new ProductStatus(3));
    Product createdProduct = createProduct(spec, product);
    List<ProductOrder> productOrders = List.of(new ProductOrder(null, createdProduct.id(), 1));

    spec.given()
        .contentType("application/json")
        .when()
        .get(PRODUCTS_ENDPOINT + "/" + createdProduct.id())
        .then()
        .statusCode(200)
        .body("status.quantity", equalTo(3));

    spec.given().contentType("application/json").body(productOrders).post(ENDPOINT);

    spec.given()
        .contentType("application/json")
        .when()
        .get(PRODUCTS_ENDPOINT + "/" + createdProduct.id())
        .then()
        .statusCode(200)
        .body("status.quantity", equalTo(2));
  }

  @Test
  void shouldCreateMultipleProductOrders(RequestSpecification spec) {
    Product product1 = new Product("test-product-order-4", ProductType.PIZZA, new ProductStatus(3));
    Product product2 = new Product("test-product-order-5", ProductType.PIZZA, new ProductStatus(3));
    Product createdProduct1 = createProduct(spec, product1);
    Product createdProduct2 = createProduct(spec, product2);
    List<ProductOrder> productOrders =
        List.of(
            new ProductOrder(null, createdProduct1.id(), 1),
            new ProductOrder(null, createdProduct2.id(), 1));

    spec.given()
        .contentType("application/json")
        .body(productOrders)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(201)
        .body(
                "productId[0]", equalTo(createdProduct1.id()),
                "quantity[0]", equalTo(1),
                "productId[1]", equalTo(createdProduct2.id()),
                "quantity[1]", equalTo(1)
        );

    spec.given()
            .contentType("application/json")
            .when()
            .get(PRODUCTS_ENDPOINT + "/" + createdProduct1.id())
            .then()
            .statusCode(200)
            .body("status.quantity", equalTo(2));

    spec.given()
            .contentType("application/json")
            .when()
            .get(PRODUCTS_ENDPOINT + "/" + createdProduct2.id())
            .then()
            .statusCode(200)
            .body("status.quantity", equalTo(2));

  }

  @Test
  void shouldNotCreateMultipleProductOrdersIfProductOrdersContainDuplicates(
      RequestSpecification spec) {
    Product product = new Product("test-product-order-6", ProductType.PIZZA, new ProductStatus(3));
    Product createdProduct = createProduct(spec, product);
    List<ProductOrder> productOrders =
        List.of(
            new ProductOrder(null, createdProduct.id(), 1),
            new ProductOrder(null, createdProduct.id(), 1));

    spec.given()
        .contentType("application/json")
        .body(productOrders)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(400);
  }

  @Test
  void shouldNotCreateMultipleProductOrdersIfOneOfProductHasNotEnoughQuantity(
      RequestSpecification spec) {
    Product product1 = new Product("test-product-order-7", ProductType.PIZZA, new ProductStatus(3));
    Product product2 = new Product("test-product-order-8", ProductType.PIZZA, new ProductStatus(3));
    Product createdProduct1 = createProduct(spec, product1);
    Product createdProduct2 = createProduct(spec, product2);
    List<ProductOrder> productOrders =
        List.of(
            new ProductOrder(null, createdProduct1.id(), 1),
            new ProductOrder(null, createdProduct2.id(), 4));

    spec.given()
        .contentType("application/json")
        .body(productOrders)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(400);
  }

  private Product createProduct(RequestSpecification spec, Product product) {
    return spec.given()
        .contentType("application/json")
        .body(product)
        .post(PRODUCTS_ENDPOINT)
        .as(Product.class);
  }
}
