package dev.folomkin.taskmanager.domain.dto.user;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Hidden
@Data
@Getter
@Setter
public class UserDto {

    @Schema(description = "Имя пользователя автора задачи")
    private String username;

    @Schema(description = "Email пользователя")
    private String email;

    private String password;
}
