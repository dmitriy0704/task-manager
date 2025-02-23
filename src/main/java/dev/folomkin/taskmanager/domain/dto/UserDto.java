package dev.folomkin.taskmanager.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserDto {

    private Long id;

    @NotBlank
    @Schema(description = "Имя пользователя автора задачи")
    private String username;

    @NotBlank
    @Schema(description = "Email пользователя")
    private String email;

}
