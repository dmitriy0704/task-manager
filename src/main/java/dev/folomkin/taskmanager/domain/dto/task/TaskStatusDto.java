package dev.folomkin.taskmanager.domain.dto.task;

import dev.folomkin.taskmanager.domain.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Статус  задачи")
@NoArgsConstructor
@Getter
@Setter
public class TaskStatusDto {
    @NotBlank(message = "Поле не должно быть пустым")
    @Schema(description = "Статус", example = "PENDING, IN_PROGRESS, FINISHED")
    private Status status;
}
