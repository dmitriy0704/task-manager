package dev.folomkin.taskmanager.service.task;

import dev.folomkin.taskmanager.domain.dto.task.TaskDto;
import dev.folomkin.taskmanager.domain.dto.task.TaskSaveDto;
import dev.folomkin.taskmanager.domain.model.Task;
import dev.folomkin.taskmanager.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface TaskService {
    Task create(TaskSaveDto taskSaveDto, User user);

    List<Task> getTasks();
    Page<Task> getAllTasksWithFilter(PageRequest request);

    Task getTask(Long id);

    Task updateStatusTask(Long id, TaskDto taskDto, User user);
    Task updateDescriptionTask(Long id, TaskDto taskDto);
    Task updateCommentsTask(Long id, TaskDto taskDto);

    Task updatePriorityTask(Long id, TaskDto taskDto);

    void deleteTask(Long id);

    List<Task> getAllUserTasks(Long userId);


}
