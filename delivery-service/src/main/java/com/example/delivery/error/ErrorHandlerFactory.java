package com.example.delivery.error;

import com.example.common.error.ErrorResponseFactory;
import com.example.common.error.JsonErrorResponseFactory;
import com.example.common.error.UncaughtExceptionHandler;
import com.example.common.error.ValidationExceptionHandler;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.hateoas.Resource;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import jakarta.validation.ValidationException;

@Factory
public class ErrorHandlerFactory {

  @Singleton
  ErrorResponseFactory jsonErrorResponseFactory() {
    return new JsonErrorResponseFactory();
  }

  @Singleton
  @Produces
  @Requires(classes = {ValidationException.class, ExceptionHandler.class})
  ExceptionHandler<ValidationException, HttpResponse<Resource>> validationExceptionHandler(
      ErrorResponseFactory errorResponseFactory) {
    return new ValidationExceptionHandler(errorResponseFactory);
  }

  @Singleton
  @Produces
  @Requires(classes = {Exception.class, ExceptionHandler.class})
  ExceptionHandler<Exception, HttpResponse<Resource>> uncaughtExceptionHandler(
      ErrorResponseFactory errorResponseFactory) {
    return new UncaughtExceptionHandler(errorResponseFactory);
  }
}
