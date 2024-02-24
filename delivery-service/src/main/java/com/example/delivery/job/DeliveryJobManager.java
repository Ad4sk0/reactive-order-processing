package com.example.delivery.job;

import com.example.models.DeliveryStatus;
import java.time.LocalDateTime;

public interface DeliveryJobManager {
  void enqueueDeliveryJob(String deliveryId);

  void updateDeliveryStatus(String deliveryId, DeliveryStatus status);

  void updateDeliveryEstimatedTime(String deliveryId, LocalDateTime estimatedTime);
}
