package com.example.delivery.job;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public final class DeliveryJobManagerImpl implements DeliveryJobManager {

  private static final Logger LOG = LoggerFactory.getLogger(DeliveryJobManagerImpl.class);

  private final DeliveryJobProducer deliveryJobProducer;

  public DeliveryJobManagerImpl(DeliveryJobProducer deliveryJobProducer) {
    this.deliveryJobProducer = deliveryJobProducer;
  }

  @Override
  public void enqueueDeliveryJob(String deliveryId) {
    LOG.info("Starting delivery job for delivery id {}", deliveryId);
    deliveryJobProducer.sendStartDeliveryMessage(deliveryId);
  }

  @Override
  public void updateDelivery(DeliveryJobStatus deliveryJobStatus) {
    LOG.info("Updating delivery job for delivery id {}", deliveryJobStatus.id());
  }
}
