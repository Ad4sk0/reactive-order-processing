package com.example.inventory.entity;

import com.example.models.AuditData;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.event.PrePersist;
import io.micronaut.data.annotation.event.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@MappedEntity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public abstract class BaseEntity {
  private @GeneratedValue @Id ObjectId id;
  private AuditData auditData;

  @PrePersist
  void prePersist() {
    this.auditData = AuditData.created();
  }

  @PreUpdate
  void preUpdate() {
    if (this.auditData == null) {
      throw new IllegalStateException("Update called on entity with no creation date");
    }
    this.auditData = this.auditData.updated();
  }
}
