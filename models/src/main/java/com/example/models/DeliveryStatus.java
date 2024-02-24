package com.example.models;

public enum DeliveryStatus {
  /** Until delivery is planned */
  INITIALIZING,

  /** After delivery job is enqueued for processing and planned for delivery */
  PLANNED,

  /** After delivery job has started */
  IN_DELIVERY,

  /** After delivery job is completed */
  DELIVERED,

  /** After delivery job is cancelled */
  CANCELLED
}
