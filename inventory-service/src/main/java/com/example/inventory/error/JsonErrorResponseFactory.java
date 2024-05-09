package com.example.inventory.error;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;
import jakarta.inject.Singleton;

@Singleton
public class JsonErrorResponseFactory implements ErrorResponseFactory {
  public JsonError createGenericResponse(HttpRequest request, String message) {
    JsonError error = new JsonError(message);
    Link selfLink = Link.build(request.getUri()).build();
    error.link(Link.SELF, selfLink);
    return error;
  }
}
