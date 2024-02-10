package com.example.inventory.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.models.ProductStatus;
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
class ProductStatusControllerTestIT implements TestPropertyProvider {

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
  void shouldRetrieveProductStatuses() {
    HttpRequest<String> httpGetRequest = HttpRequest.GET("/product-statuses");
    List<ProductStatus> productStatuses =
        client.toBlocking().retrieve(httpGetRequest, Argument.listOf(ProductStatus.class));
    assertEquals(1, productStatuses.size());
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
