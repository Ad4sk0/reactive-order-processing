package com.example.order.client;

import com.example.models.ProductOrder;
import com.example.models.ProductOrderPossibility;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import java.util.List;
import reactor.core.publisher.Mono;

@Client(id = "inventory")
public interface InventoryClient {

  @Post("/product-order-possibility")
  Mono<ProductOrderPossibility> getProductOrderPossibility(@Body List<ProductOrder> productOrder);
}
