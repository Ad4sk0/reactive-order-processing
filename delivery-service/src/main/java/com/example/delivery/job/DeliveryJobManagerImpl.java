package com.example.delivery.job;

import com.example.models.DeliveryStatus;
import jakarta.inject.Singleton;
import java.time.LocalDateTime;
import org.apache.commons.lang3.NotImplementedException;
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
  public void updateDeliveryStatus(String deliveryId, DeliveryStatus status) {
    throw new NotImplementedException();
  }

  @Override
  public void updateDeliveryEstimatedTime(String deliveryId, LocalDateTime estimatedTime) {
    throw new NotImplementedException();
  }
}
