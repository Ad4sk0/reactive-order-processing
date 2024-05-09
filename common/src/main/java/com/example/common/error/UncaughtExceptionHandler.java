package com.example.common.error;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.hateoas.*;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UncaughtExceptionHandler
    implements ExceptionHandler<Exception, HttpResponse<Resource>> {

  private final ErrorResponseFactory errorResponseFactory;

  public UncaughtExceptionHandler(ErrorResponseFactory errorResponseFactory) {
    this.errorResponseFactory = errorResponseFactory;
  }

  private static final Logger LOG = LoggerFactory.getLogger(UncaughtExceptionHandler.class);

  @Override
  public HttpResponse<Resource> handle(HttpRequest request, Exception exception) {
    LOG.error("Uncaught exception: " + exception.getMessage(), exception);
    JsonError error =
        errorResponseFactory.createGenericResponse(request, exception.getLocalizedMessage());
    return HttpResponse.serverError(error);
  }
}
