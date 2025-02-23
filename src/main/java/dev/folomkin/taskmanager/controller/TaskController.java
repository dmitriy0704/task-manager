package dev.folomkin.taskmanager.controller;

import dev.folomkin.taskmanager.domain.dto.TaskDto;
import dev.folomkin.taskmanager.domain.dto.TaskSaveDto;
import dev.folomkin.taskmanager.domain.mapper.TaskMapper;
import dev.folomkin.taskmanager.domain.model.Task;
import dev.folomkin.taskmanager.repository.TaskRepository;
import dev.folomkin.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Управление задачами", description = "API управления задачами")
@RestController
@RequestMapping("/api/v1")
@Validated
public class TaskController {
    private final TaskService taskService;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    TaskDto taskDto;

    public TaskController(TaskService taskService,
                          TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

//    @Operation(summary = "Получение списка всех задач")
//    @GetMapping("/tasks")
//    public ResponseEntity<?> tasks() {
//        return ResponseEntity.ok(taskService.getAllTasks());
//    }


    @Operation(summary = "Фильтрация и сортировка задач", description = "Для сортировки укажите поле")
    @GetMapping("/tasks")
    public Page<Task> filterTasks(
            @RequestParam(value = "offset", defaultValue = "0")
            @Min(0) @Parameter(description = "Страница с результатом") Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") @Min(1) @Max(50)
            @Parameter(description = "Количество выводимых задач. От 1 до 50") Integer limit,
            @RequestParam("sort") @Parameter(description = "Поле сортировки") String sortField
    ) {
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, sortField));
        return taskService.getAllTasks(pageRequest);
    }


    @Operation(summary = "Получение одной задачи", description = "Необходимо указать id задачи")
    @GetMapping("/task/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable("taskId")
                                        @Parameter(description = "Идентификатор задачи",
                                                required = true) Long taskId) {
        return ResponseEntity.ok().body(taskService.getTask(taskId));
    }

    @Operation(summary = "Создание задачи")
    @PostMapping("/create-task")
    public ResponseEntity<Task> create(@Valid @RequestBody TaskSaveDto taskSaveDto) {
        Task task = taskService.create(taskSaveDto);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }


    @Operation(summary = "Получение списка задач пользователя", description = "Поиск по id")
    @GetMapping("/task-user/{userId}")
    public ResponseEntity<List<Task>> getTaskByUserId(@PathVariable("userId")
                                                      @Parameter(description = "Идентификатор пользователя",
                                                              required = true) Long userId) {
        return ResponseEntity.ok().body(taskService.getAllUserTasks(userId));
    }

    @Operation(summary = "Удаление задачи", description = "Необходимо указать id задачи")
    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable("taskId")
                                             @Parameter(description = "Идентификатор задачи",
                                                     required = true) Long taskId) {
        taskService.deleteTask(taskId);
        return new ResponseEntity<>("Задача успешно удалена", HttpStatus.OK);
    }


    @Operation(summary = "Обновление статуса задачи", description = "Необходимо указать id задачи")
    @PatchMapping("/update-status/{taskId}")
    public ResponseEntity<Task> updateStatusTask(@PathVariable("taskId")
                                                 @Parameter(description = "Идентификатор задачи",
                                                         required = true) Long taskId,
                                                 @Valid @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.updateStatusTask(taskId, taskDto));
    }

    @Operation(summary = "Обновление приоритета задачи", description = "Необходимо указать id задачи")
    @PatchMapping("/update-priority/{taskId}")
    public ResponseEntity<Task> updatePriorityTask(@PathVariable("taskId")
                                                   @Parameter(description = "Идентификатор задачи",
                                                           required = true) Long taskId,
                                                   @Valid @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.updateStatusTask(taskId, taskDto));
    }


    @Operation(summary = "Обновление описания задачи", description = "Необходимо указать id задачи")
    @PatchMapping("/update-description/{taskId}")
    public ResponseEntity<Task> updateDescriptionTask(@PathVariable("taskId")
                                                      @Parameter(description = "Идентификатор задачи",
                                                              required = true) Long taskId,
                                                      @Valid @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.updateStatusTask(taskId, taskDto));
    }


    @Operation(summary = "Обновление комментария к задаче", description = "Необходимо указать id задачи")
    @PatchMapping("/update-comments/{taskId}")
    public ResponseEntity<Task> updateCommentsTask(@PathVariable("taskId")
                                                   @Parameter(description = "Идентификатор задачи",
                                                           required = true) Long taskId,
                                                   @Valid @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.updateStatusTask(taskId, taskDto));
    }


}
