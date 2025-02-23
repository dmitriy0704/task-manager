package dev.folomkin.taskmanager.service;

import dev.folomkin.taskmanager.domain.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(Long userId);
}
