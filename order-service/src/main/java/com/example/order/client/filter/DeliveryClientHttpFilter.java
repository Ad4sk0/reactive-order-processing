package com.example.order.client.filter;

import com.example.order.client.exception.DeliveryClientException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.annotation.ClientFilter;
import io.micronaut.http.filter.ClientFilterChain;
import io.micronaut.http.filter.HttpClientFilter;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@DeliveryClientFilter
@Singleton
@ClientFilter
public class DeliveryClientHttpFilter implements HttpClientFilter {

  @Override
  public Publisher<? extends HttpResponse<?>> doFilter(
      MutableHttpRequest<?> request, ClientFilterChain chain) {
    return Mono.from(chain.proceed(request))
        .flatMap(
            response -> {
              if (response.getStatus().getCode() > 400) {
                return Mono.error(
                    new IllegalStateException(
                        "Unexpected status code when calling delivery service"));
              }
              return Mono.just(response);
            })
        .onErrorMap(DeliveryClientException::new);
  }
}
