package dev.folomkin.taskmanager.domain.dto.task;

import dev.folomkin.taskmanager.domain.model.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Приоритет задачи")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskPriorityDto {

    @NotBlank(message = "Поле не должно быть пустым")
    @Schema(description = "Приоритет задачи", example = "LOW, MEDIUM, HIGH")
    private Priority priority;
}
