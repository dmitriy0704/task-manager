package dev.folomkin.taskmanager.service.task;

import dev.folomkin.taskmanager.domain.dto.task.*;
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

    Task updateStatusTask(Long id, TaskStatusDto taskDto, User user);

    Task updateDescriptionTask(Long id, TaskDescriptionDto taskDto);

    Task updateCommentsTask(Long id, TaskCommentsDto taskDto, User user);

    Task updatePriorityTask(Long id, TaskPriorityDto taskDto);

    void deleteTask(Long id);

    List<Task> getAllUserTasks(Long userId);


}
