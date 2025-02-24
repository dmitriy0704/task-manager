package dev.folomkin.taskmanager.controller;

import dev.folomkin.taskmanager.domain.dto.task.TaskDto;
import dev.folomkin.taskmanager.domain.dto.task.TaskSaveDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Управление задачами", description = "Для авторизованных пользователей")
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
    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.findAll();
    }

    @Operation(summary = "Получение списка всех пользователей постранично", description = "Только для авторизованных пользователей")
    @GetMapping("/filter-users")
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
    @GetMapping("/user/{userId}")
    public Optional<User> getUser(@PathVariable("userId")
                                  @Parameter(description = "Идентификатор пользователя", required = true)
                                  Long userId) {
        return userService.getUserById(userId);
    }


    //=== Tasks ===//
    @Operation(summary = "Получение списка всех задач", description = "Только для авторизованных пользователей")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/tasks")
    public List<Task> getTasks() {
        return taskService.getTasks();
    }


    @Operation(summary = "Получение списка всех задач постранично", description = "Только для авторизованных пользователей")
    @GetMapping("/filter-tasks")
    public Page<Task> filterTasks(
            @RequestParam(value = "offset", defaultValue = "0")
            @Min(0) @Parameter(description = "Номер страницы с результатом") Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") @Min(1) @Max(50)
            @Parameter(description = "Количество выводимых задач на странице. Минимум 1, максимум 50") Integer limit,
            @RequestParam(value = "sort", required = false) @Parameter(description = "Поле сортировки") String sortField
    ) {
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, sortField));
        return taskService.getAllTasks(pageRequest);
    }


    @Operation(summary = "Получение задачи по id", description = "Необходимо указать id задачи")
    @GetMapping("/task/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable("taskId")
                                        @Parameter(description = "Идентификатор задачи",
                                                required = true) Long taskId) {
        return ResponseEntity.ok().body(taskService.getTask(taskId));
    }


    @Operation(summary = "Создание задачи", description = "Исполнитель назначается по email")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-task")
    public ResponseEntity<Task> create(@Valid @RequestBody TaskSaveDto taskSaveDto,
                                       @AuthenticationPrincipal User user) {
        Task task = taskService.create(taskSaveDto, user);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }


    @Operation(summary = "Получение списка задач пользователя",
            description = "Поиск по id")
    @GetMapping("/task-user/{userId}")
    public ResponseEntity<List<Task>> getTaskByUserId(@PathVariable("userId")
                                                      @Parameter(description = "Идентификатор пользователя",
                                                              required = true) Long userId) {
        return ResponseEntity.ok().body(taskService.getAllUserTasks(userId));
    }

    @Operation(summary = "Удаление задачи", description = "Необходимо указать id задачи")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/task-delete/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable("taskId")
                                             @Parameter(description = "Идентификатор задачи",
                                                     required = true) Long taskId) {
        taskService.deleteTask(taskId);
        return new ResponseEntity<>("Задача успешно удалена", HttpStatus.NO_CONTENT);
    }


    @Operation(summary = "Обновление приоритета задачи", description = "Необходимо указать id задачи")
    @PatchMapping("/update-priority/{taskId}")
    public ResponseEntity<Task> updatePriorityTask(@PathVariable("taskId")
                                                   @Parameter(description = "Идентификатор задачи",
                                                           required = true) Long taskId,
                                                   @Valid @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.updatePriorityTask(taskId, taskDto));
    }


    @Operation(summary = "Обновление описания задачи", description = "Необходимо указать id задачи")
    @PatchMapping("/update-description/{taskId}")
    public ResponseEntity<Task> updateDescriptionTask(@PathVariable("taskId")
                                                      @Parameter(description = "Идентификатор задачи",
                                                              required = true) Long taskId,
                                                      @Valid @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.updateDescriptionTask(taskId, taskDto));
    }


    @Operation(summary = "Обновление статуса задачи", description = "Необходимо указать id задачи")
    @PatchMapping("/update-status/{taskId}")
    public ResponseEntity<?> updateStatusTask(@PathVariable("taskId")
                                              @Parameter(description = "Идентификатор задачи",
                                                      required = true) Long taskId,
                                              @Valid @RequestBody TaskDto taskDto,
                                              @AuthenticationPrincipal User user) {
        Task task = taskService.updateStatusTask(taskId, taskDto, user);
        if (task == null) {
            return new ResponseEntity<>(
                    messageSource.getMessage("errors.task.accessDenied", new Object[0], null),
                    HttpStatus.FORBIDDEN
            );
        }
        return ResponseEntity.ok().body(task);
    }


    @Operation(summary = "Обновление комментария к задаче", description = "Необходимо указать id задачи")
    @PatchMapping("/update-comments/{taskId}")
    public ResponseEntity<Task> updateCommentsTask(@PathVariable("taskId")
                                                   @Parameter(description = "Идентификатор задачи",
                                                           required = true) Long taskId,
                                                   @Valid @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.updateCommentsTask(taskId, taskDto));
    }


}
