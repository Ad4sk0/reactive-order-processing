package com.example.delivery.job.simulator;

import static com.example.delivery.job.QueueNames.*;
import static io.micronaut.jms.activemq.artemis.configuration.ActiveMqArtemisConfiguration.CONNECTION_FACTORY_BEAN_NAME;

import com.example.delivery.job.DeliveryJobStatus;
import io.micronaut.jms.annotations.JMSProducer;
import io.micronaut.jms.annotations.Queue;
import io.micronaut.messaging.annotation.MessageBody;

@JMSProducer(CONNECTION_FACTORY_BEAN_NAME)
public interface DeliveryJobStatusProducer {

  @Queue(UPDATE_STATUS_DELIVERY_JOB_QUEUE_NAME)
  void sendUpdateJobStatusMessage(@MessageBody DeliveryJobStatus deliveryJobStatus);
}
