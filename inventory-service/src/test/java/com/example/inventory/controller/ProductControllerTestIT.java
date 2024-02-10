package com.example.inventory.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.models.Product;
import com.example.models.ProductType;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

@Testcontainers
@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductControllerTestIT implements TestPropertyProvider {

  @Inject
  @Client("/")
  HttpClient client;

  @Container
  static final GenericContainer<?> mongoDBContainer =
      new GenericContainer<>("mongo:latest")
          .withExposedPorts(27017)
          .withCopyFileToContainer(
              MountableFile.forClasspathResource("mongo-init.js"),
              "/docker-entrypoint-initdb.d/mongo-init.js");

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

  @Override
  public @NonNull Map<String, String> getProperties() {
    String mongoUri =
        String.format(
            "mongodb://%s:%d/inventory",
            mongoDBContainer.getHost(), mongoDBContainer.getMappedPort(27017));
    return Map.of("mongodb.uri", mongoUri);
  }
}
