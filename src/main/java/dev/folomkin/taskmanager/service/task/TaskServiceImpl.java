package dev.folomkin.taskmanager.service.task;

import dev.folomkin.taskmanager.domain.dto.task.TaskDto;
import dev.folomkin.taskmanager.domain.dto.task.TaskSaveDto;
import dev.folomkin.taskmanager.domain.mapper.TaskMapper;
import dev.folomkin.taskmanager.domain.model.Task;
import dev.folomkin.taskmanager.domain.model.User;
import dev.folomkin.taskmanager.exceptions.ResourceNotFoundException;
import dev.folomkin.taskmanager.repository.TaskRepository;
import dev.folomkin.taskmanager.service.user.UserService;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final MessageSource messageSource;
    private final UserService userService;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository,
                           MessageSource messageSource,
                           UserService userService,
                           TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.messageSource = messageSource;
        this.userService = userService;
        this.taskMapper = taskMapper;
    }


    @Override
    public Page<Task> getAllTasks(PageRequest request) {
        return taskRepository.findAll(request);
    }

    @Override
    public Task getTask(Long taskId) {
        return getById(taskId);
    }

    @Override
    @Transactional
    public Task create(TaskSaveDto taskSaveDto) {
        Long userId = taskSaveDto.getUserId();
        Optional<User> user = Optional.ofNullable(userService.getUserById(userId).orElseThrow(
                () -> new ResourceNotFoundException("Пользователь с id " + userId + " не найден")
        ));
        Task task = taskMapper.map(taskSaveDto);
        task.setUser(user.get());
        taskRepository.save(task);
        return task;
    }


    @Override
    public List<Task> getAllUserTasks(Long userId) {
        return taskRepository.findTaskByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId) {
        taskRepository.delete(getById(taskId));
    }

    @Override
    public Task updateStatusTask(Long taskId, TaskDto taskDto) {
        Task changedTask = getById(taskId);
        changedTask.setStatus(taskDto.getStatus());
        taskRepository.save(changedTask);
        return changedTask;
    }


    @Override
    public Task updatePriorityTask(Long taskId, TaskDto taskDto) {
        Task changedTask = getById(taskId);
        changedTask.setPriority(taskDto.getPriority());
        taskRepository.save(changedTask);
        return changedTask;
    }

    @Override
    public Task updateDescriptionTask(Long taskId, TaskDto taskDto) {
        Task changedTask = getById(taskId);
        changedTask.setDescription(taskDto.getDescription());
        taskRepository.save(changedTask);
        return changedTask;
    }

    @Override
    public Task updateCommentsTask(Long taskId, TaskDto taskDto) {
        Task changedTask = getById(taskId);
        changedTask.setComments(taskDto.getComments());
        taskRepository.save(changedTask);
        return changedTask;
    }


    private Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("errors.404.task", new Object[0], null)));
    }


}
