package dev.folomkin.taskmanager.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class TaskResponse {

    @Schema(description = "Идентификатор задачи", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank
    @Schema(description = "Заголовок задачи")
    private String title;

    @Schema(description = "Описание задачи")
    private String description;

    @NotBlank
    @Schema(description = "Статус", example = "В ожидании, В процессе, Завершено")
    private String status;

    @NotBlank
    @Schema(description = "Приоритет задачи", example = "Низкий, Средний, Высокий")
    private String priority;

    @Schema(description = "Комментарии к задаче")
    private String comments;

    @NotBlank
    @Schema(description = "Исполнитель задачи. Идентифицируется по email")
    private String executor;

    @Schema(description = "Автор задачи")
    private String author;

    public TaskResponse() {
    }
}
