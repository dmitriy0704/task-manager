package dev.folomkin.taskmanager.domain.dto;

import dev.folomkin.taskmanager.domain.model.User;

public record TaskResponse(
        Long id,
        String title,
        String description,
        String status,
        String priority,
        String comments,
        String executor,
        User user
) {
}
