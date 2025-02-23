package dev.folomkin.taskmanager.service.user;

import dev.folomkin.taskmanager.domain.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(Long userId);
    User save(User user);
    User create(User user);

    User getByUsername(String username);
    UserDetailsService userDetailsService();
    User getCurrentUser();
}
