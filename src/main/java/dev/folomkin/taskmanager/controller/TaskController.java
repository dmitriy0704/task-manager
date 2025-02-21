package dev.folomkin.taskmanager.controller;

import dev.folomkin.taskmanager.domain.dto.TaskResponse;
import dev.folomkin.taskmanager.domain.model.Task;
import dev.folomkin.taskmanager.repository.TaskRepository;
import dev.folomkin.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TaskController {
    private final TaskService taskService;
    private final TaskRepository taskRepository;

    public TaskController(TaskService taskService, TaskRepository taskRepository) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponse>> tasks() {
        List<TaskResponse> tasks = taskService.findAll();
        return tasks.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(tasks);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id) {
        return ResponseEntity.ok().body(taskService.findById(id));
    }

    @PostMapping("/task")
    public ResponseEntity<TaskResponse> createPost(@RequestBody TaskResponse taskResponse) throws URISyntaxException {
        TaskResponse result = taskService.save(taskResponse);
        return ResponseEntity
                .created(new URI("api/v1/task/" + result.id()))
                .body(result);
    }

    @PutMapping("/post/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TaskResponse> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody TaskResponse postResponse
    ) {
        return ResponseEntity.ok().body(taskService.update(id, postResponse));
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        taskService.deleteById(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/filter")
    public Page<Task> filterBooks(
            @RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(value = "limit", defaultValue = "10") @Min(1) @Max(100) Integer limit,
            @RequestParam("sort") String sortField
    ) {
        return taskRepository.findAll(PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, sortField)));
    }
}
