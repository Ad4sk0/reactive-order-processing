package com.example.models;

import io.micronaut.serde.annotation.Serdeable;
import java.time.Instant;

@Serdeable
public record AuditData(Instant createdAt, Instant updatedAt, Instant deletedAt) {

  public static AuditData created() {
    return new AuditData(Instant.now(), null, null);
  }

  public AuditData updated() {
    return new AuditData(createdAt, Instant.now(), deletedAt);
  }

  public AuditData deleted() {
    return new AuditData(createdAt, updatedAt, Instant.now());
  }
}
