package dev.folomkin.taskmanager.domain.dto;

public record UserResponse(
        Long id,
        String username,
        String email
) {
}
