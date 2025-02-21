package dev.folomkin.taskmanager.domain.mapper;

import dev.folomkin.taskmanager.domain.dto.UserResponse;
import dev.folomkin.taskmanager.domain.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserResponse toUserResponse(Task task);
}
