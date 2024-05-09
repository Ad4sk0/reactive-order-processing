package com.example.inventory.error;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Resource;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import jakarta.validation.ValidationException;

@Produces
@Singleton
@Requires(classes = {ValidationException.class, ExceptionHandler.class})
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
