package com.example.common.error;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;

public class JsonErrorResponseFactory implements ErrorResponseFactory {
  public JsonError createGenericResponse(HttpRequest<?> request, String message) {
    JsonError error = new JsonError(message);
    Link selfLink = Link.build(request.getUri()).build();
    error.link(Link.SELF, selfLink);
    return error;
  }
}
