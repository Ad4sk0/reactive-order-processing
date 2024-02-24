package com.example.delivery.job;

import com.example.models.DeliveryStatus;
import io.micronaut.serde.annotation.Serdeable;
import java.time.LocalDateTime;

@Serdeable
public record DeliveryJobStatus(
    String id, DeliveryStatus deliveryStatus, LocalDateTime estimatedTime) {}
