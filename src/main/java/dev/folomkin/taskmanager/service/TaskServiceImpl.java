package dev.folomkin.taskmanager.service;

import dev.folomkin.taskmanager.domain.dto.TaskDto;
import dev.folomkin.taskmanager.domain.model.Status;
import dev.folomkin.taskmanager.domain.model.Task;
import dev.folomkin.taskmanager.repository.TaskRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final MessageSource messageSource;

    public TaskServiceImpl(TaskRepository taskRepository,
                           MessageSource messageSource) {
        this.taskRepository = taskRepository;
        this.messageSource = messageSource;
    }



//    @Override
//    public void getTask(String task) {
//
//    }

//    public Sample getOneSample(String taskName) {
//        return sampleRepository.findOne(taskName);
//    }

    //Finds sample by task name
//    Optional<Task> findByTaskName(String taskName) {
//        return taskRepository.findByTaskName(taskName);
//    }
//
//    //Finds sample by date created
//    List<Task> findByCreatedAt(Date createdAt) {
//        return taskRepository.findByCreatedAt(createdAt);
//    }
//
//    //Finds by id
//    Task findById(String id){
////        List<Sample> sampleList = new ArrayList<>();
////        List<Sample> sampleIds = new ArrayList<>();
//     return    tasks.stream()
//                .filter(t -> t.getId().equals(id))
//                .findFirst()
//                .get();
    ////    for (int i = 0; i < sampleList.size(); i++) {
    ////        if (sampleIds)
    ////    }
//
//    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = taskRepository.findAllTasks();
        return tasks;
    }

    @Override
    public List<Task> getAllUserTasks(Long userId) {
        List<Task> tasks = taskRepository.findTaskByUserId(userId);
        return tasks;
    }

    @Override
    public Task getTask(Long id) {
        Task task = taskRepository.findTaskById(id);
        return task;
    }

    @Override
    public Task addTask(TaskDto taskDto) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(Status.PENDING);
        taskRepository.save(task);
        return task;
    }


    @Override
    public Task updateTask(Long id, TaskDto taskDto) {
        if (taskRepository.findById(id).isPresent()) {
            Task existingTask = taskRepository.findById(id).get();
            existingTask.setTitle(taskDto.getTitle());
            existingTask.setDescription(taskDto.getDescription());
            taskRepository.save(existingTask);
            return existingTask;
        }
        return null;
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
//        tasks.removeIf(
//                t -> t.getId().equals(id)
//        );
    }

    @Override
    public Task pendingToInProgress(Long id, TaskDto taskDto) {
        if (taskRepository.findById(id).isPresent()) {
            Task pendingTask = taskRepository.findById(id).get();
            pendingTask.setStatus(taskDto.getStatus());
            taskRepository.save(pendingTask);
            return pendingTask;
        }
        return null;
    }



    @Override
    public Task InProgressBackToPending(Long id, TaskDto taskDto) {
        if (taskRepository.findById(id).isPresent()) {
            Task backToPending = taskRepository.findById(id).get();
            backToPending.setStatus(taskDto.getStatus());
            taskRepository.save(backToPending);
            return backToPending;
        }

        return null;
    }

    @Override
    public Task InProgressToDone(Long id, TaskDto taskDto) {
        if(taskRepository.findById(id).isPresent()) {
            Task progressToComplete = taskRepository.findById(id).get();
            progressToComplete.setStatus(taskDto.getStatus());
            taskRepository.save(progressToComplete);
            return progressToComplete;

        }

        return null;
    }



//    public Sample item(String taskName) {
//        Sample sample = sampleRepository.findByTitle(taskName);
//        if(taskName == null) {
//            throw new TaskNotFoundException("Task does not exist!");
//        } else {
//            return sample;
//        }
//    }

//    public Task user(String id) {
//        Task task = taskRepository.findById(id);
//        if (id == null) {
//            throw new TaskNotFoundException("Task does not exist!");
//        } else {
//            return task;
//        }
//    }


//==================================

//
//    @Override
//    public List<Task> findAll() {
//        if (taskRepository.findAll().isEmpty()) {
//            throw new TaskNotFoundException(
//                    messageSource.getMessage("errors.404.tasks", new Object[0], Locale.getDefault())
//            );
//        }
//        return taskRepository.findAll();
//    }
//
//    @Override
//    public TaskDto findById(Long id) {
//        return Optional.of(getById(id)).map(taskMapper::toTaskResponse).get();
//    }
//
//
//    @Override
//    @Transactional
//    public Task save(TaskDto taskDto) {
//        return taskRepository.save(taskMapper.toTaskModel(taskDto));
//    }
//
//    @Override
//    @Transactional
//    public Task update(Long id, TaskDto taskDto) {
//        Task task = this.getById(id);
//        if (taskDto.getComments() != null) {
//            task.setComments(taskDto.getComments());
//        }
//        if (taskDto.getDescription() != null) {
//            task.setDescription(taskDto.getDescription());
//        }
//        if (taskDto.getExecutor() != null) {
//            task.setExecutor(taskDto.getExecutor());
//        }
//        if (taskDto.getPriority() != null) {
//            task.setPriority(taskDto.getPriority());
//        }
//        return taskRepository.save(task);
//    }

//    @Override
//    @Transactional
//    public void deleteById(Long id) {
//        var task = getById(id);
//        taskRepository.delete(task);
//    }
//
//    private Task getById(Long id) {
//        return taskRepository.findById(id)
//                .orElseThrow(() -> new TaskNotFoundException(
//                        messageSource.getMessage("errors.404.task", new Object[0], null))
//                );
//    }
}
