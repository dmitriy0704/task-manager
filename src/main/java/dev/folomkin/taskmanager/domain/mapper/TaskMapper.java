package dev.folomkin.taskmanager.domain.mapper;

import dev.folomkin.taskmanager.domain.dto.TaskResponse;
import dev.folomkin.taskmanager.domain.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {
    Task toTaskModel(TaskResponse taskResponse);
    TaskResponse toTaskResponse(Task task);
    List<TaskResponse> toListResponse(List<Task> tasks);
}
