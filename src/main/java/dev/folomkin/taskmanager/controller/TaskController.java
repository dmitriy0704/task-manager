package dev.folomkin.taskmanager.controller;

import dev.folomkin.taskmanager.domain.dto.TaskDto;
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

@Tag(name = "Управление задачами", description = "API управления задачами")
@RestController
@RequestMapping("/api/v1")
@Validated
public class TaskController {
    private final TaskService taskService;
    private final TaskRepository taskRepository;

    TaskDto taskDto;

    public TaskController(TaskService taskService,
                          TaskRepository taskRepository) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }

    @Operation(summary = "Получение списка всех задач")
    @GetMapping("/tasks")
    public ResponseEntity<?> tasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @Operation(summary = "Получение одной задачи", description = "Необходимо указать id задачи")
    @GetMapping("/task/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable("taskId")
            @Parameter(description = "Идентификатор задачи", required = true) Long taskId) {
        return ResponseEntity.ok().body(taskService.getTask(taskId));
    }

    @Operation(summary = "Создание задачи")
    @PostMapping("/addtask")
    public ResponseEntity<TaskDto> addTask(@Valid @RequestBody TaskDto taskDto) {
        taskService.addTask(taskDto);
        return new ResponseEntity<>(taskDto, HttpStatus.CREATED);
    }


    @Operation(summary = "Обновление задачи", description = "Необходимо указать id задачи")
    @PutMapping("/update/{taskId}")
    public ResponseEntity<String> updateTask(@PathVariable("taskId")
                                             @Parameter(description = "Идентификатор задачи", required = true) Long taskId,
                                             @Valid @RequestBody TaskDto taskDto) {
        taskService.updateTask(taskId, taskDto);
        return new ResponseEntity<>("Task updated successfully!", HttpStatus.OK);
    }

    @Operation(summary = "Удаление задачи", description = "Необходимо указать id задачи")
    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable("taskId")
                                             @Parameter(description = "Идентификатор задачи", required = true) Long taskId) {
        taskService.deleteTask(taskId);
        return new ResponseEntity<>("Задача успешно удалена", HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Изменение статуса задачи",
            description = "Изменение статуса задачи с \"В ожидании\" на \"В процессе\" по id задачи")
    @PutMapping("/status/progress/{taskId}")
    public ResponseEntity<String> pendingToProgress(@PathVariable("taskId")
                                                    @Parameter(description = "Идентификатор задачи", required = true) Long taskId, @RequestBody TaskDto taskDto) {
        taskService.pendingToInProgress(taskId, taskDto);
        return new ResponseEntity<>("Статус задачи успешно изменен!", HttpStatus.OK);
    }

    @Operation(summary = "Изменение статуса задачи",
            description = "Изменение статуса задачи с  \"В процессе\" на \"В ожидании\" по id задачи")
    @PutMapping("/status/pending/{taskId}")
    public ResponseEntity<String> inProgressToPending(@PathVariable("taskId")
                                                      @Parameter(description = "Идентификатор задачи", required = true) Long taskId, @RequestBody TaskDto taskDto) {
        taskService.InProgressBackToPending(taskId, taskDto);
        return new ResponseEntity<>("Статус задачи успешно изменен!", HttpStatus.OK);
    }

    @Operation(summary = "Изменение статуса задачи",
            description = "Изменение статуса задачи с  \"В процессе\" на \"Завершено\" по id задачи")
    @PutMapping("/status/done/{taskId}")
    public ResponseEntity<String> inProgressToDone(@PathVariable("taskId") @Parameter(description = "Идентификатор задачи", required = true) Long taskId, @RequestBody TaskDto taskDto) {
        taskService.InProgressToDone(taskId, taskDto);
        return new ResponseEntity<>("Task status changed successfully", HttpStatus.OK);
    }


    @Operation(summary = "Фильтрация и сортировка задач", description = "Для сортировки укажите поле")
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
