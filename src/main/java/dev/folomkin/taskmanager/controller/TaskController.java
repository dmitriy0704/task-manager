package dev.folomkin.taskmanager.controller;

import dev.folomkin.taskmanager.domain.dto.task.*;
import dev.folomkin.taskmanager.domain.dto.user.UserResponseDto;
import dev.folomkin.taskmanager.domain.model.Task;
import dev.folomkin.taskmanager.domain.model.User;
import dev.folomkin.taskmanager.service.task.TaskService;
import dev.folomkin.taskmanager.service.user.UserService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Управление задачами", description = "Только для авторизованных пользователей")
@RestController
@RequestMapping("/api/v1")
@Validated
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final MessageSource messageSource;

    public TaskController(TaskService taskService, UserService userService, MessageSource messageSource) {
        this.taskService = taskService;
        this.userService = userService;
        this.messageSource = messageSource;
    }

    //=== Users ====/

    @Operation(summary = "Получение списка всех пользователей", description = "Только для авторизованных пользователей")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/get-users", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsers() {
        return userService.findAll();
    }

    @Operation(summary = "Получение списка всех пользователей постранично", description = "Только для авторизованных пользователей")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/filter-users", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<UserResponseDto> filterUser(
            @RequestParam(value = "offset", defaultValue = "0")
            @Min(0) @Parameter(description = "Номер страницы с результатом") Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") @Min(1) @Max(50)
            @Parameter(description = "Количество выводимых пользователей на странице. Минимум 1, максимум 50") Integer limit,
            @RequestParam(value = "sort") @Parameter(description = "Поле сортировки") String sortField
    ) {
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, sortField));
        return userService.findAllByFilter(pageRequest);
    }

    @Operation(summary = "Получение пользователя по id", description = "Только для авторизованных пользователей")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/get-user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<User> getUser(@PathVariable("userId")
                                  @Parameter(description = "Идентификатор пользователя", required = true) Long userId) {
        return userService.getUserById(userId);
    }


    //=== Tasks ===//
    @Operation(summary = "Получение списка всех задач", description = "Только для авторизованных пользователей")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/get-tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Task> getTasks() {
        return taskService.getTasks();
    }


    @Operation(summary = "Получение списка всех задач постранично", description = "Только для авторизованных пользователей")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/filter-tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Task> filterTasks(
            @RequestParam(value = "offset", defaultValue = "0")
            @Min(0) @Parameter(description = "Номер страницы с результатом") Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") @Min(1) @Max(50)
            @Parameter(description = "Количество выводимых задач на странице. Минимум 1, максимум 50") Integer limit,
            @RequestParam(value = "sort") @Parameter(description = "Поле сортировки") String sortField,
            @RequestParam(value = "executor", required = false) @Parameter(description = "Исполнитель указанный в задаче") String executor,
            @RequestParam(value = "author", required = false) @Parameter(description = "Email автора") String author
    ) {
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, sortField));
        return taskService.getAllTasksWithFilter(pageRequest, executor, author);
    }

    @Operation(summary = "Получение задачи по id", description = "Необходимо указать id задачи")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/get-task-id/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Task getTaskById(@PathVariable("taskId")
                            @Parameter(description = "Идентификатор задачи", required = true) Long taskId) {
        return taskService.getTaskById(taskId);
    }

    @Operation(summary = "Получение задач исполнителя", description = "Необходимо указать email исполнителя")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/get-task-executor/{executor}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Task> getTaskByExecutor(@PathVariable("executor")
                                        @Parameter(description = "Email исполнителя", required = true) String executor) {
        return taskService.getTaskByExecutor(executor);
    }


    @Operation(summary = "Получение списка задач по id автора", description = "Поиск по id")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/user-task/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Task> getTaskByUserId(@PathVariable("userId")
                                      @Parameter(description = "Идентификатор пользователя", required = true) Long userId) {
        return taskService.getAllTasksByAuthorId(userId);
    }

    @Operation(summary = "Создание задачи", description = "Исполнитель назначается по email")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/create-task", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody TaskSaveDto taskSaveDto,
                                    @AuthenticationPrincipal User user) {
        Task task = taskService.create(taskSaveDto, user);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @Operation(summary = "Удаление задачи", description = "Необходимо указать id задачи")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/delete-task/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteTask(@PathVariable("taskId")
                                             @Parameter(description = "Идентификатор задачи", required = true) Long taskId) {
        taskService.deleteTask(taskId);
        return new ResponseEntity<>("Задача успешно удалена", HttpStatus.NO_CONTENT);
    }


    @Operation(summary = "Обновление приоритета задачи",
            description = "Необходимо указать id задачи и одно из доступных значений приоритета")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/update-task/priority/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> updatePriorityTask(
            @PathVariable("taskId") @Parameter(description = "Идентификатор задачи", required = true) Long taskId,
            @RequestBody @Parameter(description = "Приоритет задачи", required = true) TaskPriorityDto taskDto) {
        return ResponseEntity.ok(taskService.updatePriorityTask(taskId, taskDto));
    }


    @Operation(summary = "Обновление описания задачи", description = "Необходимо указать id задачи")
    @PreAuthorize(value = "hasRole('ADMIN')")
    @PutMapping(value = "/update-task/description/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> updateDescriptionTask(
            @PathVariable("taskId") @Parameter(description = "Идентификатор задачи", required = true) Long taskId,
            @Valid @RequestBody @Parameter(description = "Описание задачи", required = true) TaskDescriptionDto taskDto) {
        return ResponseEntity.ok(taskService.updateDescriptionTask(taskId, taskDto));
    }


    @PreAuthorize(value = "hasRole('ADMIN')")
    @Operation(summary = "Обновление исполнителя", description = "Необходимо указать id задачи")
    @PutMapping(value = "/update-task/executor/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> updateExecutorTask(
            @PathVariable("taskId") @Parameter(description = "Идентификатор задачи", required = true) Long taskId,
            @Valid @RequestBody @Parameter(description = "Новый исполнитель задачи", required = true) TaskExecutorDto taskDto) {
        return ResponseEntity.ok(taskService.updateExecutorTask(taskId, taskDto));
    }


    @Operation(summary = "Обновление статуса задачи",
            description = "Необходимо указать id задачи и одно из доступных значений статуса")
    @PutMapping(value = "/update-task/status/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateStatusTask(
            @PathVariable("taskId")
            @Parameter(description = "Идентификатор задачи", required = true) Long taskId,
            @RequestBody @Parameter(description = "Статус задачи", required = true) TaskStatusDto newStatus,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(taskService.updateStatusTask(taskId, newStatus, user));
    }


    @Operation(summary = "Обновление комментария к задаче", description = "Необходимо указать id задачи")
    @PutMapping(value = "/update-task/comments/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> updateCommentsTask(
            @PathVariable("taskId") @Parameter(description = "Идентификатор задачи", required = true) Long taskId,
            @Valid @RequestBody @Parameter(description = "Комментарии к задаче", required = true) TaskCommentsDto taskDto,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.updateCommentsTask(taskId, taskDto, user));
    }


    /**
     * Используется для детализации ошибок полей DTO
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
