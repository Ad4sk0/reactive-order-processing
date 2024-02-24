package com.example.delivery.job;

public interface DeliveryJobManager {
  void enqueueDeliveryJob(String deliveryId);

  void updateDelivery(DeliveryJobStatus deliveryJobStatus);
}
