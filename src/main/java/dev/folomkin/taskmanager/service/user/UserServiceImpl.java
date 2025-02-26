package dev.folomkin.taskmanager.service.user;

import dev.folomkin.taskmanager.domain.dto.user.UserResponseDto;
import dev.folomkin.taskmanager.domain.mapper.UserMapper;
import dev.folomkin.taskmanager.domain.model.Role;
import dev.folomkin.taskmanager.domain.model.User;
import dev.folomkin.taskmanager.exceptions.AuthExistUserException;
import dev.folomkin.taskmanager.exceptions.NoSuchElementException;
import dev.folomkin.taskmanager.exceptions.CustomAuthenticationException;
import dev.folomkin.taskmanager.repository.UserRepository;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MessageSource messageSource;

    public UserServiceImpl(UserRepository userRepository,
                           MessageSource messageSource) {
        this.userRepository = userRepository;
        this.messageSource = messageSource;
    }


    @Override
    public List<User> findAll() {
        if (userRepository.findAll().isEmpty()) {
            throw new NoSuchElementException(
                    messageSource
                            .getMessage("errors.404.userList", new Object[0], null)
            );
        }
        return userRepository.findAll();
    }

    @Override
    public Page<UserResponseDto> findAllByFilter(PageRequest request) {
        return new PageImpl<>(userRepository
                .findAll(request)
                .stream()
                .map(UserMapper::toUserDto)
                .toList());
    }


    @Override
    public Optional<User> getUserById(Long userId) {
        return Optional.ofNullable(userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException(
                        messageSource.getMessage("errors.404.user", new Object[0], null)
                )
        ));
    }


    /**
     * Сохранение пользователя
     *
     * @return сохраненный пользователь
     */
    public User save(User user) {
        return userRepository.save(user);
    }


    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new AuthExistUserException("Пользователь с таким именем уже существует");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AuthExistUserException("Пользователь с таким email уже существует");
        }
        return save(user);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    public User getByUsername(String username) {
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new CustomAuthenticationException("Пользователь не найден"));
    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    /**
     * Выдача прав администратора текущему пользователю
     * <p>
     * Нужен для демонстрации
     */
    @Deprecated
    public void getAdmin() {
        var user = getCurrentUser();
        user.setRole(Role.ROLE_ADMIN);
        save(user);
    }

}
