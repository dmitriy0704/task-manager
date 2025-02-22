package dev.folomkin.taskmanager.service;

import dev.folomkin.taskmanager.domain.dto.TaskResponse;
import dev.folomkin.taskmanager.domain.model.Task;

import java.util.List;

public interface TaskService {

    List<Task> getAllUserTasks(Long userId);

    List<TaskResponse> findAll();

    TaskResponse findById(Long id);

    TaskResponse update(Long id, TaskResponse task);
    Task save(TaskResponse task);

    void deleteById(Long id);
}
