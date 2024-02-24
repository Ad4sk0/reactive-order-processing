package com.example.delivery.job.simulator;

import static com.example.delivery.job.QueueNames.START_DELIVERY_JOB_QUEUE_NAME;
import static io.micronaut.jms.activemq.artemis.configuration.ActiveMqArtemisConfiguration.CONNECTION_FACTORY_BEAN_NAME;

import io.micronaut.jms.annotations.JMSListener;
import io.micronaut.jms.annotations.Queue;
import io.micronaut.messaging.annotation.MessageBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JMSListener(CONNECTION_FACTORY_BEAN_NAME)
class DeliveryCreateJobConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(DeliveryCreateJobConsumer.class);

  @Queue(START_DELIVERY_JOB_QUEUE_NAME)
  public void receiveStartDeliveryMessage(@MessageBody String deliveryId) {
    LOG.info("Simulator: Received message on {}: {}", START_DELIVERY_JOB_QUEUE_NAME, deliveryId);
  }
}
