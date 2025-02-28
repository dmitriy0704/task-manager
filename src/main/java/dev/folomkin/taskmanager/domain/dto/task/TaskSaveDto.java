package dev.folomkin.taskmanager.domain.dto.task;

import dev.folomkin.taskmanager.domain.model.Priority;
import dev.folomkin.taskmanager.domain.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskSaveDto {

    @NotBlank(message = "Заголовок не должен быть пустым")
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

    @Email(regexp = ".+@.+\\..+", message = "Email адрес должен быть в формате user@example.com")
    @NotBlank(message = "Назначьте исполнителя")
    @Schema(description = "Исполнитель задачи. Идентифицируется по email")
    private String executor;

}
