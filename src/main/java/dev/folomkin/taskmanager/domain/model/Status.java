package dev.folomkin.taskmanager.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Статусы задачи")
public enum Status {
    PENDING,
    IN_PROGRESS,
    FINISHED
}
