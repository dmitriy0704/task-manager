package dev.folomkin.taskmanager.domain.mapper;

import dev.folomkin.taskmanager.domain.dto.task.TaskSaveDto;
import dev.folomkin.taskmanager.domain.model.Task;
import dev.folomkin.taskmanager.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public Task map(TaskSaveDto taskSaveDto, User user) {
        Task task = new Task();
        task.setTitle(taskSaveDto.getTitle());
        task.setDescription(taskSaveDto.getDescription());
        task.setPriority(taskSaveDto.getPriority());
        task.setComments(taskSaveDto.getComments());
        task.setStatus(taskSaveDto.getStatus());
        task.setExecutor(taskSaveDto.getExecutor());
        task.setAuthor(user);
        return task;
    }
}
