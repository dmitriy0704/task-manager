package dev.folomkin.taskmanager.domain.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Новый исполнитель")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskExecutorDto {

    @Email(regexp = ".+@.+\\..+", message = "Email адрес должен быть в формате user@example.com")
    @NotBlank(message = "Назначьте исполнителя")
    @Schema(description = "Исполнитель задачи. Идентифицируется по email")
    private String executor;

}
