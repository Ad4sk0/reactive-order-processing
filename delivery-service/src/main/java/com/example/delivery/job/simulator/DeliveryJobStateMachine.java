package com.example.delivery.job.simulator;

import com.example.delivery.job.DeliveryJobStatus;
import jakarta.inject.Singleton;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DeliveryJobStateMachine {
  private static final Logger LOG = LoggerFactory.getLogger(DeliveryJobStateMachine.class);
  private static final ScheduledExecutorService executorService =
      new ScheduledThreadPoolExecutor(5);
  private static final Random random = new Random();
  private final DeliveryJobStatusProducer deliveryJobStatusProducer;

  public DeliveryJobStateMachine(DeliveryJobStatusProducer deliveryJobStatusProducer) {
    this.deliveryJobStatusProducer = deliveryJobStatusProducer;
  }

  public void initJobExecution(String deliveryId) {
    LOG.info("Simulator: Initialize delivery job for delivery id {}", deliveryId);
    var state = DeliveryJobState.INIT;
    executeCurrentStateAndPlanNext(deliveryId, state.getNextState(), getRandomStateDelay(state));
  }

  private void executeNextState(String deliveryId, DeliveryJobState state) {
    LOG.debug("Simulator: Executing step {} for job : {}", state, deliveryId);
    var deliveryJobStatus = createCurrentDeliveryJobStatus(deliveryId, state);
    LOG.info("Simulator: Current job status : {}", deliveryJobStatus);
    sendJobStatusUpdate(deliveryJobStatus);
    if (state.getNextState() != null) {
      LOG.trace("Simulator: next planed step is {} for job : {}", state.getNextState(), deliveryId);
      executeCurrentStateAndPlanNext(deliveryId, state.getNextState(), getRandomStateDelay(state));
    }
  }

  private DeliveryJobStatus createCurrentDeliveryJobStatus(
      String deliveryId, DeliveryJobState state) {
    LocalDateTime estimatedTime;
    if (state.getNextState() != null) {
      estimatedTime = LocalDateTime.now().plusSeconds(getEstimatedTimeUntilEnd(state));
    } else {
      estimatedTime = null;
    }
    return new DeliveryJobStatus(deliveryId, state.getDeliveryStatus(), estimatedTime);
  }

  private void executeCurrentStateAndPlanNext(String body, DeliveryJobState state, long delay) {
    executorService.schedule(() -> executeNextState(body, state), delay, TimeUnit.SECONDS);
  }

  private void sendJobStatusUpdate(DeliveryJobStatus deliveryJobStatus) {
    deliveryJobStatusProducer.sendUpdateJobStatusMessage(deliveryJobStatus);
  }

  private long getEstimatedTimeUntilEnd(DeliveryJobState state) {
    long sum = 0;
    while (state != null) {
      sum += getAverageStateDelay(state);
      state = state.getNextState();
    }
    return sum;
  }

  private static long getRandomStateDelay(DeliveryJobState state) {
    return random.nextLong(state.getMinExecutionTime(), state.getMaxExecutionTime());
  }

  private static long getAverageStateDelay(DeliveryJobState state) {
    return (state.getMaxExecutionTime() - state.getMinExecutionTime()) / 2L;
  }
}
