package dev.folomkin.taskmanager.repository;

import dev.folomkin.taskmanager.domain.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTaskByUserId(Long id);

}
