package com.example.inventory.controller;

import static org.hamcrest.Matchers.*;

import com.example.inventory.AbstractContainersTest;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

class ProductControllerTestIT extends AbstractContainersTest {

  @Test
  void shouldCreateAndRetrieveProductTmp(RequestSpecification spec) {
    spec.given()
        .contentType("application/json")
        .body(
            """
                  {
                    "product": {
                      "name": "test123",
                      "productType": "PIZZA"
                    }
                  }""")
        .when()
        .post("/products")
        .then()
        .statusCode(201)
        .body(
            "name", equalTo("test123"),
            "id", notNullValue());

    spec.when().get("/products").then().statusCode(200).body("name", hasItems("test", "test123"));
  }
}
