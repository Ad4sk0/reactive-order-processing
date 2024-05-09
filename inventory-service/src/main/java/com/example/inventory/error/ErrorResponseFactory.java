package com.example.inventory.error;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.hateoas.JsonError;

public interface ErrorResponseFactory {

  JsonError createGenericResponse(HttpRequest request, String message);
}
