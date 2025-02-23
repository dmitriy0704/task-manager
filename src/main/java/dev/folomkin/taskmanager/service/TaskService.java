package dev.folomkin.taskmanager.service;

import dev.folomkin.taskmanager.domain.dto.TaskDto;
import dev.folomkin.taskmanager.domain.dto.TaskSaveDto;
import dev.folomkin.taskmanager.domain.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface TaskService {
    Task create(TaskSaveDto taskSaveDto);

    Page<Task> getAllTasks(PageRequest request);

    Task getTask(Long id);

    Task updateStatusTask(Long id, TaskDto taskDto);

    Task updatePriorityTask(Long id, TaskDto taskDto);

    void deleteTask(Long id);

    List<Task> getAllUserTasks(Long userId);


}
