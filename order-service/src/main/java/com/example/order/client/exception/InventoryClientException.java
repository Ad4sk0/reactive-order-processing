package com.example.order.client.exception;

public class InventoryClientException extends RuntimeException {

  public InventoryClientException(String message) {
    super(message);
  }

  public InventoryClientException(String message, Throwable cause) {
    super(message, cause);
  }

  public InventoryClientException(Throwable cause) {
    super(cause);
  }
}
