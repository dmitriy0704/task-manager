package dev.folomkin.taskmanager.domain.dto.task;

import dev.folomkin.taskmanager.domain.model.Priority;
import dev.folomkin.taskmanager.domain.model.Status;
import dev.folomkin.taskmanager.domain.model.Task;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private String comments;
    private String executor;
    private String author;

    public static TaskDto toDTO(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setTitle(task.getTitle());
        taskDto.setDescription(task.getDescription());
        taskDto.setStatus(task.getStatus());
        taskDto.setPriority(task.getPriority());
        taskDto.setComments(task.getComments());
        taskDto.setExecutor(task.getExecutor());
        return taskDto;
    }
}
