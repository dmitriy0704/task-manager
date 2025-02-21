package dev.folomkin.taskmanager.service;

import dev.folomkin.taskmanager.domain.dto.TaskResponse;
import dev.folomkin.taskmanager.domain.mapper.TaskMapper;
import dev.folomkin.taskmanager.domain.model.Task;
import dev.folomkin.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }


    @Override
    public List<TaskResponse> findAll() {
        return taskMapper.toListResponse(taskRepository.findAll());
    }

    @Override
    public TaskResponse findById(Long id) {
        return Optional.of(getById(id)).map(taskMapper::toTaskResponse).get();
    }

    @Override
    @Transactional
    public TaskResponse update(Long id, TaskResponse taskResponse) {
        return null;
    }

    @Override
    @Transactional
    public TaskResponse save(TaskResponse taskResponse) {
        return taskMapper.toTaskResponse(taskRepository.save(taskMapper.toTaskModel(taskResponse)));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        var task = getById(id);
        taskRepository.delete(task);
    }

    private Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Задача с id: " + id + " не найдена"));
    }
}
