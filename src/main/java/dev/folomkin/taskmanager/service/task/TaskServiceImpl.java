package dev.folomkin.taskmanager.service.task;

import dev.folomkin.taskmanager.domain.dto.task.*;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                           TaskMapper taskMapper, UserRepository userRepository
    ) {
        this.taskRepository = taskRepository;
        this.messageSource = messageSource;
        this.userService = userService;
        this.taskMapper = taskMapper;
        this.userRepository = userRepository;
    }


    // ===== USERS ===== //

    /**
     * Получение задач по id автора
     *
     * @param userId id автора
     */
    @Override
    public List<Task> getAllTasksByAuthorId(Long userId) {
        Optional<User> user = Optional.ofNullable(userService.getUserById(userId).orElseThrow(
                // Пользователь не найден
                () -> new NoSuchElementException(messageSource.getMessage("errors.404.user", new Object[0], null))
        ));

        List<Task> userTasks = taskRepository.findTaskByAuthorId(user.get().getId());
        if (userTasks.isEmpty()) {
            // У пользователя нет задач
            throw new NoSuchElementException(messageSource.getMessage("errors.404.user.tasks", new Object[0], null));
        }
        return taskRepository.findTaskByAuthorId(userId);
    }


    // ===== TASKS ==== //

    /**
     * Получение всех задач без сортировки
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

    /**
     * Получение всех задач с возможностью сортировки и пагинации
     *
     * @param request параметры сортировки и пагинации:
     *                offset - номер страницы
     *                limit - количество выводимых записей на странице
     *                sortField - поле сортировки задач(обязательное)
     */
    @Override
    public Page<Task> getAllTasksWithFilter(PageRequest request, String executor, String author) {
        if (taskRepository.findAll(request).isEmpty()) {
            throw new NoSuchElementException(messageSource.getMessage("errors.404.taskList", new Object[0], null));
        }

        if (executor != null) {
            return new PageImpl<>(taskRepository.findAll(request)
                    .stream().filter(
                            t ->
                                    t.getExecutor().equalsIgnoreCase(executor)
                    ).collect(Collectors.toList())
            );
        }

        if (author != null) {
            return new PageImpl<>(taskRepository.findAll(request)
                    .stream().filter(
                            t -> t.getAuthor().getEmail().equalsIgnoreCase(author)
                    ).collect(Collectors.toList())
            );
        }
        return taskRepository.findAll(request);
    }

    /**
     * Получение задачи по id
     *
     * @param taskId id задачи
     */
    @Override
    public Task getTaskById(Long taskId) {
        return getById(taskId);
    }

    /**
     * Получение задач исполнителя
     *
     * @param executor исполнитель, указанный в задаче
     */
    @Override
    public List<Task> getTaskByExecutor(String executor) {
        List<Task> tasks = taskRepository.findAllByExecutor(executor);
        if (tasks.isEmpty()) {
            throw new NoSuchElementException(messageSource.getMessage("errors.404.executor", new Object[0], null));
        }
        return tasks;
    }

    /**
     * Создание новой задачи
     *
     * @param taskSaveDto тело запроса
     * @param user        объект текущего пользователя
     *                    для проверки прав на создание задачи
     */
    @Override
    @Transactional
    public Task create(TaskSaveDto taskSaveDto, User user) {
        Task task = taskRepository.findByTitle(taskSaveDto.getTitle());
        if (task == null) {
            return taskRepository.save(taskMapper.map(taskSaveDto, user));
        }
        throw new InvalidTaskFieldException(messageSource.getMessage("errors.400.dublicateTask", new Object[0], null));
    }

    /**
     * Удаление задачи по id
     *
     * @param taskId id задачи
     */
    @Override
    @Transactional
    public void deleteTask(Long taskId) {
        taskRepository.delete(getById(taskId));
    }

    /**
     * Обновление приоритета задачи по id
     *
     * @param taskId  id задачи
     * @param taskDto тело запроса с новым приоритетом
     * @return changedTask - обновленная задача
     */
    @Override
    public Task updatePriorityTask(Long taskId, TaskPriorityDto taskDto) {
        Task changedTask = getById(taskId);
        changedTask.setPriority(taskDto.getPriority());
        taskRepository.save(changedTask);
        return changedTask;
    }

    /**
     * Обновление описания задачи по id
     *
     * @param taskId  id задачи
     * @param taskDto тело запроса с новым описанием
     * @return changedTask - обновленная задача
     */
    @Override
    public Task updateDescriptionTask(Long taskId, TaskDescriptionDto taskDto) {
        Task changedTask = getById(taskId);
        changedTask.setDescription(taskDto.getDescription());
        taskRepository.save(changedTask);
        return changedTask;
    }

    /**
     * Обновление статуса задачи по id
     *
     * @param taskId  id задачи
     * @param taskDto тело запроса с новым статусом
     * @return changedTask - обновленная задача
     * @throws ChangeTaskIsExecutorException пользователь не назначен исполнителем задачи
     */
    @Override
    public Task updateStatusTask(Long taskId, TaskStatusDto taskDto, User user) {
        Task changedTask = getById(taskId);
        if (isExecutor(changedTask, user) || user.getRole() == Role.ROLE_ADMIN) {
            changedTask.setStatus(taskDto.getStatus());
            taskRepository.save(changedTask);
            return changedTask;
        }
        throw new ChangeTaskIsExecutorException(messageSource.getMessage("errors.403.task.change.executor", new Object[0], null));
    }

    /**
     * Обновление комментария к задаче по id
     *
     * @param taskId  id задачи
     * @param taskDto тело запроса с новым комментарием
     * @return changedTask - обновленная задача
     * @throws ChangeTaskIsExecutorException пользователь не назначен исполнителем задачи
     */
    @Override
    public Task updateCommentsTask(Long taskId, TaskCommentsDto taskDto, User user) {
        Task changedTask = getById(taskId);
        if (isExecutor(changedTask, user) || user.getRole() == Role.ROLE_ADMIN) {
            changedTask.setComments(taskDto.getComments());
            taskRepository.save(changedTask);
            return changedTask;
        }
        throw new ChangeTaskIsExecutorException(messageSource.getMessage("errors.403.task.change.executor", new Object[0], null));
    }

    /**
     * Проверка - является ли текущий пользователь исполнителем задачи
     *
     * @param taskToChange задача
     * @param user         текущий пользователь
     */
    public boolean isExecutor(Task taskToChange, User user) {
        boolean isExecutorUser = taskToChange.getExecutor().equalsIgnoreCase(user.getEmail());
        if (isExecutorUser) {
            return true;
        }
        return false;
    }

    /**
     * Поиск задачи по id для использования внутри класса
     *
     * @param id задачи
     * @throws NoSuchElementException задача не найдена
     */
    private Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        messageSource.getMessage("errors.404.task", new Object[0], null)));
    }
}
