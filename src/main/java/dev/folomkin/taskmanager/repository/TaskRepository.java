package dev.folomkin.taskmanager.repository;

import dev.folomkin.taskmanager.domain.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
