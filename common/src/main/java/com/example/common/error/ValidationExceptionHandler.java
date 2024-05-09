package com.example.common.error;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Resource;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.validation.ValidationException;

public class ValidationExceptionHandler
    implements ExceptionHandler<ValidationException, HttpResponse<Resource>> {

  private final ErrorResponseFactory errorResponseFactory;

  public ValidationExceptionHandler(ErrorResponseFactory errorResponseFactory) {
    this.errorResponseFactory = errorResponseFactory;
  }

  @Override
  public HttpResponse<Resource> handle(HttpRequest request, ValidationException exception) {
    JsonError error =
        errorResponseFactory.createGenericResponse(request, exception.getLocalizedMessage());
    return HttpResponse.badRequest(error);
  }
}
