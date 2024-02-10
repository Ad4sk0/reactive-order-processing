package com.example.inventory.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.inventory.AbstractContainersTest;
import com.example.models.Product;
import com.example.models.ProductType;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Inject;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProductControllerTestIT extends AbstractContainersTest {

  @Inject
  @Client("/")
  HttpClient client;

  @Test
  void shouldCreateAndRetrieveProduct() {
    HttpRequest<String> httpPostRequest =
        HttpRequest.POST(
            "/products",
            "{\"product\": {\n"
                + "        \"name\": \"test1\",\n"
                + "        \"productType\": \"PIZZA\"\n"
                + "    }}");
    Product createdProduct = client.toBlocking().retrieve(httpPostRequest, Product.class);
    assertNotNull(createdProduct.id());
    assertEquals("test1", createdProduct.name());
    assertEquals(ProductType.PIZZA, createdProduct.productType());

    HttpRequest<String> httpGetRequest = HttpRequest.GET("/products");
    List<Product> products =
        client.toBlocking().retrieve(httpGetRequest, Argument.listOf(Product.class));
    assertEquals(createdProduct.id(), products.get(1).id());
  }
}
