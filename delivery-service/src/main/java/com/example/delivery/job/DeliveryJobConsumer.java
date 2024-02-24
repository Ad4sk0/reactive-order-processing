package com.example.delivery.job;

import static com.example.delivery.job.QueueNames.UPDATE_STATUS_DELIVERY_JOB_QUEUE_NAME;
import static io.micronaut.jms.activemq.artemis.configuration.ActiveMqArtemisConfiguration.CONNECTION_FACTORY_BEAN_NAME;

import io.micronaut.jms.annotations.JMSListener;
import io.micronaut.jms.annotations.Queue;
import io.micronaut.messaging.annotation.MessageBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JMSListener(CONNECTION_FACTORY_BEAN_NAME)
class DeliveryJobConsumer {
  private static final Logger LOG = LoggerFactory.getLogger(DeliveryJobConsumer.class);
  private final DeliveryJobManager deliveryJobManager;

  public DeliveryJobConsumer(DeliveryJobManager deliveryJobManager) {
    this.deliveryJobManager = deliveryJobManager;
  }

  @Queue(UPDATE_STATUS_DELIVERY_JOB_QUEUE_NAME)
  public void receiveUpdateDeliveryJobStatus(@MessageBody DeliveryJobStatus deliveryJobStatus) {
    LOG.debug(
        "Received message on {}: {}", UPDATE_STATUS_DELIVERY_JOB_QUEUE_NAME, deliveryJobStatus);
    deliveryJobManager.updateDelivery(deliveryJobStatus);
  }
}
