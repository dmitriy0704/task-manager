package dev.folomkin.taskmanager.controller;

import dev.folomkin.taskmanager.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
@RequiredArgsConstructor
@Tag(name = "Демонстрация", description = "Для текущего авторизованного пользователя")
public class ExampleController {
    private final UserService service;
//
//    @GetMapping
//    @Operation(summary = "Доступен только авторизованным пользователям")
//    public String example() {
//        return "Hello, world!";
//    }


    @GetMapping("/set-admin")
    @Operation(summary = "Стать Админом")
    public void getAdmin() {
        service.getAdmin();
    }


    @GetMapping("/admin")
    @Operation(summary = "Доступен только авторизованным пользователям с ролью ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public String exampleAdmin() {
        return "Hello, admin!";
    }
}