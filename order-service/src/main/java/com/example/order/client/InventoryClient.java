package com.example.order.client;

import com.example.models.ProductOrder;
import com.example.models.ProductOrderCancellation;
import com.example.models.ProductOrderPossibility;
import com.example.order.client.exception.InventoryClientException;
import com.example.order.client.filter.InventoryClientFilter;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Client(id = "inventory")
@InventoryClientFilter
public interface InventoryClient {

  @Post("/product-order-possibility")
  Mono<ProductOrderPossibility> getProductOrderPossibility(@Body List<ProductOrder> productOrder);

  @Post("/product-orders")
  Flux<ProductOrder> createProductOrder(@Body List<ProductOrder> productOrder)
      throws InventoryClientException;

  @Post("/product-order-cancellations")
  Flux<ProductOrderCancellation> createProductOrderCancellations(
      @Body List<ProductOrderCancellation> productOrderCancellations);
}
