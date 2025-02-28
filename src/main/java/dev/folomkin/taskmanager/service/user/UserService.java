package dev.folomkin.taskmanager.service.user;

import dev.folomkin.taskmanager.domain.dto.user.UserResponseDto;
import dev.folomkin.taskmanager.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Page<UserResponseDto> findAllByFilter(PageRequest request);

    Optional<User> getUserById(Long userId);

    User save(User user);

    User create(User user);

    User getByUsername(String username);

    UserDetailsService userDetailsService();

    User getCurrentUser();

    void getAdmin();
    void getUser();
}
