package com.example.order.client.exception;

public class DeliveryClientException extends RuntimeException {

  public DeliveryClientException(String message) {
    super(message);
  }

  public DeliveryClientException(String message, Throwable cause) {
    super(message, cause);
  }

  public DeliveryClientException(Throwable cause) {
    super(cause);
  }
}
