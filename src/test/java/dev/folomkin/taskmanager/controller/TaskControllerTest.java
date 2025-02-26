package dev.folomkin.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.folomkin.taskmanager.config.security.SecurityConfiguration;
import dev.folomkin.taskmanager.domain.dto.task.TaskSaveDto;
import dev.folomkin.taskmanager.domain.model.*;
import dev.folomkin.taskmanager.repository.TaskRepository;
import dev.folomkin.taskmanager.repository.UserRepository;
import dev.folomkin.taskmanager.service.security.JwtService;
import dev.folomkin.taskmanager.service.task.TaskService;
import dev.folomkin.taskmanager.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@Import({SecurityConfiguration.class})
class TaskControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthenticationProvider authenticationProvider;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    UserRepository userRepository;

    @MockitoBean
    private TaskRepository taskRepository;


    @DisplayName("Получение пользователя по id авторизованным пользователем")
    @Test
    @WithMockUser(username = "username", roles = {"ADMIN"})
    void getUserWithAuthorized() throws Exception {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User(
                        1L,
                        "username",
                        "password",
                        "email@email.com",
                        new Date(),
                        Role.ROLE_USER
                )));
        mockMvc.perform(get("/api/v1/get-user/{userId}", 1L))
                .andExpect(status().isOk());
    }

    @DisplayName("Получение пользователя по id неавторизованным пользователем")
    @Test
    @WithAnonymousUser
    void cannotGetCustomerIfNotAuthorized() throws Exception {
        mockMvc.perform(get("/api/v1/get-user/{userId}", 1L))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Удаление задачи по id авторизованным пользователем")
    @Test
    void adminCanDeleteCustomer() throws Exception {


        mockMvc.perform(delete("/api/v1/delete-task/{taskId}", 1L)
                .with(csrf())
                .with(user("admin").roles("ADMIN"))
        ).andExpect(status().isNoContent());
    }

    @DisplayName("Удаление задачи по id неавторизованным пользователем")
    @Test
    void cannotDeleteCustomerIfNotAuthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/delete-task/{taskId}", 1L)
                .with(csrf())
                .with(anonymous())
        ).andExpect(status().isUnauthorized());
    }

    @DisplayName("Получение задачи по id авторизованным пользователем")
    @Test
    @WithMockUser(username = "username", roles = {"ADMIN"})
    void getTasksAuthUser() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("username");
        user1.setPassword("password");
        user1.setEmail("email@email.com");
        user1.setCreateDate(new Date());
        user1.setRole(Role.ROLE_USER);
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(new Task(
                        1L,
                        "title1",
                        "read book",
                        new Date(),
                        new Date(),
                        Status.PENDING,
                        Priority.HIGH,
                        "new comments",
                        user1,
                        "user@mail.ru"

                )));
        mockMvc.perform(get("/api/v1/get-task/{taskId}", 1L))
                .andExpect(status().isOk());
    }


    @DisplayName("Получение задачи по id неавторизованным пользователем")
    @Test
    void getTasksUnAuthUser() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("username");
        user1.setPassword("password");
        user1.setEmail("email@email.com");
        user1.setCreateDate(new Date());
        user1.setRole(Role.ROLE_USER);
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(new Task(
                        1L,
                        "title1",
                        "read book",
                        new Date(),
                        new Date(),
                        Status.PENDING,
                        Priority.HIGH,
                        "new comments",
                        user1,
                        "user@mail.ru"
                )));
        mockMvc.perform(get("/api/v1/get-task/{taskId}", 1L)
                        .with(anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Создание задачи авторизованным пользователем")
    @Test
    void createTaskShouldReturnCreatedTask_AuthUser() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("username");
        user1.setPassword("password");
        user1.setEmail("email@email.com");
        user1.setCreateDate(new Date());
        user1.setRole(Role.ROLE_USER);

        Task tsd1 = new Task(
                1L,
                "title",
                "read book",
                new Date(),
                new Date(),
                Status.PENDING,
                Priority.HIGH,
                "new comments",
                user1,
                "user@mail.ru"
        );

        TaskSaveDto taskSaveDto = new TaskSaveDto();
        taskSaveDto.setTitle("title");
        taskSaveDto.setComments("comments");
        taskSaveDto.setPriority(Priority.HIGH);
        taskSaveDto.setStatus(Status.PENDING);
        taskSaveDto.setDescription("description");
        taskSaveDto.setExecutor("executor");
        when(this.taskService.create(taskSaveDto, null)).thenReturn(tsd1);
        mockMvc.perform(post("/api/v1/create-task")
                        .with(csrf())
                        .with(user("admin").roles("ADMIN"))
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(taskSaveDto)));
    }

    @DisplayName("Создание задачи неавторизованным пользователем")
    @Test
    void createTaskShouldReturnCreatedTask_UnauthorizedUser() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("username");
        user1.setPassword("password");
        user1.setEmail("email@email.com");
        user1.setCreateDate(new Date());
        user1.setRole(Role.ROLE_USER);

        Task tsd1 = new Task(
                1L,
                "title1",
                "read book",
                new Date(),
                new Date(),
                Status.PENDING,
                Priority.HIGH,
                "new comments",
                user1,
                "user@mail.ru"
        );

        TaskSaveDto taskSaveDto = new TaskSaveDto();
        taskSaveDto.setTitle("title2");
        taskSaveDto.setComments("comments");
        taskSaveDto.setPriority(Priority.HIGH);
        taskSaveDto.setStatus(Status.PENDING);
        taskSaveDto.setDescription("description");
        taskSaveDto.setExecutor("executor");
        when(this.taskService.create(taskSaveDto, null)).thenReturn(tsd1);
        mockMvc.perform(post("/api/v1/create-task")
                        .with(csrf())
                        .with(anonymous())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(taskSaveDto)))
                .andExpect(status().isUnauthorized());
    }


    @DisplayName("Просмотр списка задач авторизованным пользователем")
    @Test
    void findAllShouldReturnAllTasksAuthUser() throws Exception {
        mockMvc.perform(get("/api/v1/get-tasks")
                .with(csrf())
                .with(user("admin").roles("ADMIN"))
        ).andExpect(status().isOk());
    }

    @DisplayName("Просмотр списка задач неавторизованным пользователе")
    @Test
    void findAllShouldReturnAllTasksNoAuthUser() throws Exception {
        mockMvc.perform(get("/api/v1/get-tasks")
                .with(csrf())
                .with(anonymous())
        ).andExpect(status().isUnauthorized());
    }
}