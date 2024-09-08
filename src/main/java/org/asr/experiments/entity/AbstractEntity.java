package org.asr.experiments.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AbstractEntity {

    @Builder.Default
    String id = UUID.randomUUID().toString();
    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();
    @Builder.Default
    LocalDateTime updatedAt = LocalDateTime.now();
    @Builder.Default
    String createdBy = "system";
    @Builder.Default
    String updatedBy = "system";
}
