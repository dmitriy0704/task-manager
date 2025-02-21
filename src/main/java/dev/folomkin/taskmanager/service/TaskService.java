package dev.folomkin.taskmanager.service;

import dev.folomkin.taskmanager.domain.dto.TaskResponse;

import java.util.List;

public interface TaskService {

    List<TaskResponse> findAll();

    TaskResponse findById(Long id);

    TaskResponse update(Long id, TaskResponse task);
    TaskResponse save(TaskResponse task);

    void deleteById(Long id);
}
