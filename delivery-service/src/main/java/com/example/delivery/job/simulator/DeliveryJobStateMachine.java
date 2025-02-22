package com.example.delivery.job.simulator;

import static com.example.delivery.job.simulator.DeliveryJobStateUtils.getEstimatedTimeUntilEnd;

import com.example.delivery.job.DeliveryJobStatus;
import com.example.models.DeliveryStatus;
import jakarta.inject.Singleton;
import java.time.Instant;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
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
  private final Set<String> cancelledJobs = new ConcurrentSkipListSet<>();

  public DeliveryJobStateMachine(DeliveryJobStatusProducer deliveryJobStatusProducer) {
    this.deliveryJobStatusProducer = deliveryJobStatusProducer;
  }

  public void initJobExecution(String deliveryId) {
    LOG.info("Simulator: Initialize delivery job for delivery id {}", deliveryId);
    var state = DeliveryJobState.INIT;
    executeCurrentStateAndPlanNext(deliveryId, state.getNextState(), getRandomStateDelay(state));
  }

  public void cancelJobExecution(String deliveryId) {
    LOG.info("Simulator: Cancel delivery job for delivery id {}", deliveryId);
    cancelledJobs.add(deliveryId);
  }

  private void executeNextState(String deliveryId, DeliveryJobState state) {
    LOG.debug("Simulator: Executing step {} for job : {}", state, deliveryId);
    if (isCancelled(deliveryId)) {
      LOG.debug("Simulator: Stopping execution for job : {}", deliveryId);
      cancelledJobs.remove(deliveryId);
      sendJobStatusUpdate(
          new DeliveryJobStatus(deliveryId, DeliveryStatus.CANCELLED, null, Instant.now()));
      return;
    }
    var deliveryJobStatus = createCurrentDeliveryJobStatus(deliveryId, state);
    LOG.info("Simulator: Current job status : {}", deliveryJobStatus);
    sendJobStatusUpdate(deliveryJobStatus);
    if (state.getNextState() != null) {
      LOG.trace("Simulator: next planed step is {} for job : {}", state.getNextState(), deliveryId);
      executeCurrentStateAndPlanNext(deliveryId, state.getNextState(), getRandomStateDelay(state));
    }
  }

  private boolean isCancelled(String deliveryId) {
    return cancelledJobs.contains(deliveryId);
  }

  private DeliveryJobStatus createCurrentDeliveryJobStatus(
      String deliveryId, DeliveryJobState state) {
    Instant estimatedTime;
    Instant endTime;
    if (state.getNextState() != null) {
      estimatedTime = Instant.now().plusSeconds(getEstimatedTimeUntilEnd(state));
      endTime = null;
    } else {
      estimatedTime = null;
      endTime = Instant.now();
    }
    return new DeliveryJobStatus(deliveryId, state.getDeliveryStatus(), estimatedTime, endTime);
  }

  private void executeCurrentStateAndPlanNext(String body, DeliveryJobState state, long delay) {
    executorService.schedule(() -> executeNextState(body, state), delay, TimeUnit.SECONDS);
  }

  private void sendJobStatusUpdate(DeliveryJobStatus deliveryJobStatus) {
    deliveryJobStatusProducer.sendUpdateJobStatusMessage(deliveryJobStatus);
  }

  private static long getRandomStateDelay(DeliveryJobState state) {
    return random.nextLong(state.getMinExecutionTime(), state.getMaxExecutionTime());
  }
}
