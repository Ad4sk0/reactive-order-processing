package com.example.delivery.job;

import com.example.delivery.event.DeliveryCanceledEvent;
import com.example.delivery.event.DeliveryCreatedEvent;
import com.example.delivery.event.DeliveryEvent;
import com.example.delivery.service.DeliveryService;
import com.example.models.DeliveryStatus;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public final class DeliveryJobManagerImpl implements DeliveryJobManager {

  private static final Logger LOG = LoggerFactory.getLogger(DeliveryJobManagerImpl.class);

  private final DeliveryJobProducer deliveryJobProducer;
  private final DeliveryService deliveryService;

  public DeliveryJobManagerImpl(
      DeliveryJobProducer deliveryJobProducer, DeliveryService deliveryService) {
    this.deliveryJobProducer = deliveryJobProducer;
    this.deliveryService = deliveryService;
  }

  @Override
  public void updateDelivery(DeliveryJobStatus deliveryJobStatus) {
    LOG.info("Updating delivery job for delivery id {}", deliveryJobStatus.deliveryId());
    deliveryService
        .updateStatusAndEstimatedDeliveryTime(deliveryJobStatus)
        .map(
            updatedCount -> {
              if (updatedCount != 1) {
                throw new IllegalStateException(
                    String.format(
                        "Unable to update delivery job for delivery %s. Updated count: %d",
                        deliveryJobStatus.deliveryId(), updatedCount));
              }
              return updatedCount;
            })
        .doOnError(
            e ->
                LOG.error(
                    "Unable to update delivery job for delivery id {}",
                    deliveryJobStatus.deliveryId(),
                    e))
        .subscribe();
    if (deliveryJobStatus.deliveryStatus() == DeliveryStatus.DELIVERED
        || deliveryJobStatus.deliveryStatus() == DeliveryStatus.CANCELLED) {
      LOG.info(
          "Delivery job for delivery id {} has been completed with status {}",
          deliveryJobStatus.deliveryId(),
          deliveryJobStatus);
      deliveryService
          .completeDelivery(deliveryJobStatus)
          .map(
              updatedCount -> {
                if (updatedCount != 1) {
                  throw new IllegalStateException(
                      String.format(
                          "Unable to complete delivery job for delivery id %s. Updated count: %d",
                          deliveryJobStatus.deliveryId(), updatedCount));
                }
                return updatedCount;
              })
          .doOnError(
              e ->
                  LOG.error(
                      "Unable to complete delivery job for delivery id {}",
                      deliveryJobStatus.deliveryId(),
                      e))
          .subscribe();
    }
  }

  @EventListener
  public void onDeliveryEvent(DeliveryEvent deliveryEvent) {
    if (deliveryEvent instanceof DeliveryCreatedEvent deliveryCreatedEvent) {
      enqueueDeliveryJob(deliveryCreatedEvent.deliveryId());
    } else if (deliveryEvent instanceof DeliveryCanceledEvent deliveryCanceledEvent) {
      enqueueDeliveryCanceledJob(deliveryCanceledEvent.deliveryId());
    } else {
      LOG.warn("Unknown delivery event: {}", deliveryEvent);
    }
  }

  private void enqueueDeliveryJob(String deliveryId) {
    LOG.info("Starting delivery job for delivery id {}", deliveryId);
    deliveryJobProducer.sendStartDeliveryMessage(deliveryId);
  }

  private void enqueueDeliveryCanceledJob(String deliveryId) {
    LOG.info("Starting delivery cancel job for delivery id {}", deliveryId);
    deliveryJobProducer.sendCancelDeliveryMessage(deliveryId);
  }
}
