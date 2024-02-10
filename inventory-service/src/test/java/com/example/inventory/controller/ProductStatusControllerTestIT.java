package com.example.inventory.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.inventory.AbstractContainersTest;
import com.example.models.ProductStatus;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Inject;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProductStatusControllerTestIT extends AbstractContainersTest {

  @Inject
  @Client("/")
  HttpClient client;

  @Test
  void shouldRetrieveProductStatuses() {
    HttpRequest<String> httpGetRequest = HttpRequest.GET("/product-statuses");
    List<ProductStatus> productStatuses =
        client.toBlocking().retrieve(httpGetRequest, Argument.listOf(ProductStatus.class));
    assertEquals(1, productStatuses.size());
  }
}
