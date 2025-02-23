package dev.folomkin.taskmanager.domain.dto;

import dev.folomkin.taskmanager.domain.model.Priority;
import dev.folomkin.taskmanager.domain.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class TaskSaveDto {

    @NotBlank
    @Schema(description = "Заголовок задачи")
    private String title;

    @Schema(description = "Описание задачи")
    private String description;

    @NotNull
    @Schema(description = "Статус", example = "PENDING, IN_PROGRESS, FINISHED")
    private Status status;

    @NotNull
    @Schema(description = "Приоритет задачи", example = "LOW, MEDIUM, HIGH")
    private Priority priority;

    @Schema(description = "Комментарии к задаче")
    private String comments;

    @NotBlank
    @Schema(description = "Исполнитель задачи. Идентифицируется по email")
    private String executor;

    @Schema(description = "Автор задачи")
    private String author;

    @NotNull(message = "Поле не должно быть пустым")
    private Long userId;
}
