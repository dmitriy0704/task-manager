package dev.folomkin.taskmanager.service.task;

import dev.folomkin.taskmanager.domain.dto.task.TaskDto;
import dev.folomkin.taskmanager.domain.dto.task.TaskSaveDto;
import dev.folomkin.taskmanager.domain.mapper.TaskMapper;
import dev.folomkin.taskmanager.domain.model.Role;
import dev.folomkin.taskmanager.domain.model.Task;
import dev.folomkin.taskmanager.domain.model.User;
import dev.folomkin.taskmanager.exceptions.ChangeTaskIsExecutorException;
import dev.folomkin.taskmanager.exceptions.InvalidTaskFieldException;
import dev.folomkin.taskmanager.exceptions.NoSuchElementException;
import dev.folomkin.taskmanager.repository.TaskRepository;
import dev.folomkin.taskmanager.repository.UserRepository;
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
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository,
                           MessageSource messageSource,
                           UserService userService,
                           TaskMapper taskMapper, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.messageSource = messageSource;
        this.userService = userService;
        this.taskMapper = taskMapper;
        this.userRepository = userRepository;
    }


    /**
     * Получение всех задач без фильтрации
     *
     * @return список задач или исключение о том, что список пуст
     */
    @Override
    public List<Task> getTasks() {
        if (taskRepository.findAll().isEmpty()) {
            throw new NoSuchElementException(messageSource.getMessage("errors.404.taskList", new Object[0], null));
        }
        return taskRepository.findAll();
    }

    @Override
    public Page<Task> getAllTasksWithFilter(PageRequest request) {
        if (taskRepository.findAll(request).isEmpty()) {
            throw new NoSuchElementException(messageSource.getMessage("errors.404.taskList", new Object[0], null));
        }
        return taskRepository.findAll(request);
    }

    @Override
    public Task getTask(Long taskId) {
        return getById(taskId);
    }

    @Override
    @Transactional
    public Task create(TaskSaveDto taskSaveDto, User user) {
        Task task = taskRepository.findByTitle(taskSaveDto.getTitle());
        if (task == null) {
            return taskRepository.save(taskMapper.map(taskSaveDto, user));
        }
        throw new InvalidTaskFieldException(messageSource.getMessage("errors.400.dublicateTask", new Object[0], null));
    }


    @Override
    public List<Task> getAllUserTasks(Long userId) {
        Optional<User> user = Optional.ofNullable(userService.getUserById(userId).orElseThrow(
                () -> new NoSuchElementException(messageSource.getMessage("errors.404.user", new Object[0], null))
        ));

        List<Task> userTasks = taskRepository.findTaskByAuthorId(user.get().getId());
        if (userTasks.isEmpty()) {
            throw new NoSuchElementException(messageSource.getMessage("errors.404.user.tasks", new Object[0], null));
        }
        return taskRepository.findTaskByAuthorId(userId);
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId) {
        taskRepository.delete(getById(taskId));
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
    public Task updateStatusTask(Long taskId, TaskDto taskDto, User user) {
        Task changedTask = getById(taskId);
        if (isExecutor(changedTask, user) || user.getRole() == Role.ROLE_ADMIN) {
            changedTask.setStatus(taskDto.getStatus());
            taskRepository.save(changedTask);
            return changedTask;
        }
        throw new ChangeTaskIsExecutorException(messageSource.getMessage("errors.403.task.change.executor", new Object[0], null));
    }


    @Override
    public Task updateCommentsTask(Long taskId, TaskDto taskDto, User user) {
        Task changedTask = getById(taskId);
        if (isExecutor(changedTask, user) || user.getRole() == Role.ROLE_ADMIN) {
            changedTask.setComments(taskDto.getComments());
            taskRepository.save(changedTask);
            return changedTask;
        }
        throw new ChangeTaskIsExecutorException(messageSource.getMessage("errors.403.task.change.executor", new Object[0], null));
    }


    public boolean isExecutor(Task taskToChange, User user) {
        boolean isExecutorUser = taskToChange.getExecutor().equalsIgnoreCase(user.getEmail());
        if (isExecutorUser) {
            return true;
        }
        return false;
    }

    private Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        messageSource.getMessage("errors.404.task", new Object[0], null)));
    }


}
