package dev.folomkin.taskmanager.service;

import dev.folomkin.taskmanager.domain.dto.TaskResponse;
import dev.folomkin.taskmanager.domain.mapper.TaskMapper;
import dev.folomkin.taskmanager.domain.model.Task;
import dev.folomkin.taskmanager.exceptions.TaskNotFoundException;
import dev.folomkin.taskmanager.repository.TaskRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final MessageSource messageSource;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, MessageSource messageSource) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.messageSource = messageSource;
    }

    @Override
    public List<Task> getAllUserTasks(Long userId) {
        List<Task> tasks = taskRepository.findTaskByUserId(userId);
        return tasks;
    }

    @Override
    public List<TaskResponse> findAll() {
        if (taskRepository.findAll().isEmpty()) {
            throw new TaskNotFoundException(
                    messageSource.getMessage("errors.404.tasks", new Object[0], Locale.getDefault())
            );
        }
        return taskMapper.toListResponse(taskRepository.findAll());
    }

    @Override
    public TaskResponse findById(Long id) {
        return Optional.of(getById(id)).map(taskMapper::toTaskResponse).get();
    }


    @Override
    @Transactional
    public Task save(TaskResponse taskResponse) {
        return taskRepository.save(taskMapper.toTaskModel(taskResponse));
    }

    @Override
    @Transactional
    public TaskResponse update(Long id, TaskResponse taskResponse) {
        Task task = this.getById(id);
        if (taskResponse.getComments() != null) {
            task.setComments(taskResponse.getComments());
        }
        if (taskResponse.getDescription() != null) {
            task.setDescription(taskResponse.getDescription());
        }
        if (taskResponse.getExecutor() != null) {
            task.setExecutor(taskResponse.getExecutor());
        }
        if (taskResponse.getPriority() != null) {
            task.setPriority(taskResponse.getPriority());
        }
        return taskMapper.toTaskResponse(taskRepository.save(task));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        var task = getById(id);
        taskRepository.delete(task);
    }

    private Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(
                        messageSource.getMessage("errors.404.task", new Object[0], null))
                );
    }
}
