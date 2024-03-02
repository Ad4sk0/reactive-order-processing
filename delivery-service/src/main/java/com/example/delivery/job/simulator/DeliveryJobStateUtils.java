package com.example.delivery.job.simulator;

public class DeliveryJobStateUtils {

  private DeliveryJobStateUtils() {}

  public static long getEstimatedDeliveryTime() {
    return getEstimatedTimeUntilEnd(DeliveryJobState.INIT);
  }

  static long getEstimatedTimeUntilEnd(DeliveryJobState state) {
    long sum = 0;
    while (state != null) {
      sum += getAverageStateDelay(state);
      state = state.getNextState();
    }
    return sum;
  }

  private static long getAverageStateDelay(DeliveryJobState state) {
    return (state.getMaxExecutionTime() - state.getMinExecutionTime()) / 2L;
  }
}
