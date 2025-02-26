package dev.folomkin.taskmanager.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Приоритеты задачи")
public enum Priority {
    LOW, MEDIUM, HIGH;
}
