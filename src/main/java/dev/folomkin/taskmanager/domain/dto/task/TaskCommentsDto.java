package dev.folomkin.taskmanager.domain.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Комментарии к задаче")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCommentsDto {

    @NotBlank(message = "Поле не должно быть пустым")
    @Schema(description = "Комментарии к задаче")
    private String comments;

}
