package dev.folomkin.taskmanager.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Task Manager System Api",
                description = "API системы управления задачами",
                version = "1.0.0",
                contact = @Contact(
                        name = "Folomkin Dmitriy",
                        email = "dmitriy.folomkin@yandex.ru",
                        url = "https://folomkin.dev"
                )
        )
)
public class OpenApiConfig {
    // Конфигурация для Swagger
}