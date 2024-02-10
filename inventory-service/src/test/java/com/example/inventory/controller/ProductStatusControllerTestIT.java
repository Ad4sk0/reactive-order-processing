package com.example.inventory.controller;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

import com.example.inventory.AbstractContainersTest;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

class ProductStatusControllerTestIT extends AbstractContainersTest {
  @Test
  void shouldRetrieveProductStatuses(RequestSpecification spec) {
    spec.when()
        .get("/product-statuses")
        .then()
        .statusCode(200)
        .body(
            "$", hasSize(1),
            "id", hasItems("65c77c64c0df697183d064b5"));
  }
}
