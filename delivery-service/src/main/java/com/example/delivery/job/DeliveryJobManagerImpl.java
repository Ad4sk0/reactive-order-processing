package com.example.delivery.job;

import com.example.delivery.event.DeliveryCreatedEvent;
import io.micronaut.runtime.event.annotation.EventListener;
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
  public void updateDelivery(DeliveryJobStatus deliveryJobStatus) {
    LOG.info("Updating delivery job for delivery id {}", deliveryJobStatus.deliveryId());
  }

  @EventListener
  public void onDeliveryCreatedEvent(DeliveryCreatedEvent event) {
    enqueueDeliveryJob(event.deliveryId());
  }

  private void enqueueDeliveryJob(String deliveryId) {
    LOG.info("Starting delivery job for delivery id {}", deliveryId);
    deliveryJobProducer.sendStartDeliveryMessage(deliveryId);
  }
}
