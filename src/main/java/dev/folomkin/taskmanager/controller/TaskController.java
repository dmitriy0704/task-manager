package dev.folomkin.taskmanager.controller;

import dev.folomkin.taskmanager.domain.dto.TaskResponse;
import dev.folomkin.taskmanager.domain.model.Task;
import dev.folomkin.taskmanager.repository.TaskRepository;
import dev.folomkin.taskmanager.service.TaskServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@Tag(name = "Управление задачами", description = "API управления задачами")
@RestController
@RequestMapping("/api/v1")
@Validated
public class TaskController {
    private final TaskServiceImpl taskService;
    private final TaskRepository taskRepository;
    private final MessageSource messageSource;


    public TaskController(TaskServiceImpl taskService, TaskRepository taskRepository, MessageSource messageSource) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
        this.messageSource = messageSource;
    }

    @Operation(
            summary = "Получение списка всех задач",
            description = ""
    )
    @GetMapping("/tasks")
    public ResponseEntity<?> tasks() {
        return ResponseEntity.ok(taskService.findAll());
    }

    @Operation(
            summary = "Получение одной задачи",
            description = "Необходимо указать id задачи"
    )
    @GetMapping("/task/{id}")
    public ResponseEntity<TaskResponse> getTask(
            @PathVariable
            @Parameter(description = "Идентификатор задачи", required = true)
            Long id) {
        return ResponseEntity.ok().body(taskService.findById(id));
    }

    @Operation(
            summary = "Создание задачи",
            description = ""
    )
    @PostMapping("/task")
    public ResponseEntity<Task> createPost(
            @Valid @RequestBody TaskResponse taskResponse) throws URISyntaxException {
        Task result = taskService.save(taskResponse);
        return ResponseEntity
                .created(new URI("api/v1/task/" + result.getId()))
                .body(result);
    }

    @Operation(
            summary = "Обновление задачи",
            description = "Необходимо указать id задачи"
    )
    @PatchMapping("/task/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updatePost(
            @PathVariable("id")
            @Parameter(description = "Идентификатор задачи", required = true) Long id,
            @Valid @RequestBody
            @Parameter(description = "Поля для обновления",
                    example = "Статус: В ожидании"
            )
            TaskResponse taskResponse
    ) {
        taskService.update(id, taskResponse);
        return new ResponseEntity<>("Задача успешно обновлена", HttpStatus.OK);
    }


    @Operation(
            summary = "Удаление задачи",
            description = "Необходимо указать id задачи"
    )
    @DeleteMapping("/task/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") @Parameter(description = "Идентификатор задачи", required = true) Long id) {
        taskService.deleteById(id);
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "Фильтрация и сортировка задач",
            description = "Для сортировки укажите поле"
    )
    @GetMapping("/filter")
    public Page<Task> filterBooks(
            @RequestParam(value = "offset", defaultValue = "0")
            @Min(0) @Parameter(description = "Страница с результатом", required = true) Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") @Min(1) @Max(50) @Parameter(description = "Количество выводимых задач. От 1 до 50", required = true) Integer limit,
            @RequestParam("sort") @Parameter(description = "Поле сортировки", required = true) String sortField
    ) {
        return taskRepository.findAll(PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, sortField)));
    }
}
