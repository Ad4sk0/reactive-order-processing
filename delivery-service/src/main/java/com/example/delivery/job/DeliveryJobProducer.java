package com.example.delivery.job;

import static com.example.delivery.job.QueueNames.CANCEL_DELIVERY_JOB_QUEUE_NAME;
import static com.example.delivery.job.QueueNames.START_DELIVERY_JOB_QUEUE_NAME;
import static io.micronaut.jms.activemq.artemis.configuration.ActiveMqArtemisConfiguration.CONNECTION_FACTORY_BEAN_NAME;

import io.micronaut.jms.annotations.JMSProducer;
import io.micronaut.jms.annotations.Queue;
import io.micronaut.messaging.annotation.MessageBody;

@JMSProducer(CONNECTION_FACTORY_BEAN_NAME)
public interface DeliveryJobProducer {

  @Queue(START_DELIVERY_JOB_QUEUE_NAME)
  void sendStartDeliveryMessage(@MessageBody String deliveryId);

  @Queue(CANCEL_DELIVERY_JOB_QUEUE_NAME)
  void sendCancelDeliveryMessage(@MessageBody String deliveryId);
}
