package dev.folomkin.taskmanager.service;

import dev.folomkin.taskmanager.domain.dto.TaskDto;
import dev.folomkin.taskmanager.domain.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskService {
    List<Task> tasks = new ArrayList<>();

    // List<Task> getAllUserTasks(String userId);

    List<Task> getAllUserTasks(Long userId);

    List<Task> getAllTasks();

    public Task getTask(Long id);

    public Task addTask(TaskDto taskDto);

    public Task updateTask(Long id, TaskDto taskDto);

    public void deleteTask(Long id);

    public Task pendingToInProgress(Long id, TaskDto taskDto);

    public Task InProgressBackToPending(Long id, TaskDto taskDto);

    public Task InProgressToDone(Long id, TaskDto taskDto);

}
