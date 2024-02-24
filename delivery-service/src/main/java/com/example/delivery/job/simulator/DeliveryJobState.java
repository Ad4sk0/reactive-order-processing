package com.example.delivery.job.simulator;

import com.example.models.DeliveryStatus;
import lombok.Getter;

@Getter
enum DeliveryJobState {
  INIT(DeliveryStatus.INITIALIZING, 1, 10) {
    @Override
    public DeliveryJobState getNextState() {
      return PLANNED;
    }
  },

  PLANNED(DeliveryStatus.PLANNED, 10, 20) {
    @Override
    public DeliveryJobState getNextState() {
      return IN_DELIVERY;
    }
  },
  IN_DELIVERY(DeliveryStatus.IN_DELIVERY, 10, 20) {
    @Override
    public DeliveryJobState getNextState() {
      return DELIVERED;
    }
  },
  DELIVERED(DeliveryStatus.DELIVERED, -1, -1) {
    @Override
    public DeliveryJobState getNextState() {
      return null;
    }
  };

  private final DeliveryStatus deliveryStatus;
  private final long minExecutionTime;
  private final long maxExecutionTime;

  DeliveryJobState(DeliveryStatus deliveryStatus, long minExecutionTime, long maxExecutionTime) {
    this.deliveryStatus = deliveryStatus;
    this.minExecutionTime = minExecutionTime;
    this.maxExecutionTime = maxExecutionTime;
  }

  public abstract DeliveryJobState getNextState();
}
