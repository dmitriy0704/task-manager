package dev.folomkin.taskmanager.repository;

import dev.folomkin.taskmanager.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
