package dev.folomkin.taskmanager.controller;

import dev.folomkin.taskmanager.domain.model.Priority;
import dev.folomkin.taskmanager.domain.model.Status;
import dev.folomkin.taskmanager.domain.model.Task;
import dev.folomkin.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class TaskControllerIT {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)).build();
    }


    @DisplayName("Получение списка всех задач")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnAllTasks() throws Exception {
        mockMvc.perform(get("/api/v1/get-tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @DisplayName("Получение задачи по id")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnOneTasks() throws Exception {
        //Given
        Task task = new Task(
                null,
                "title1",
                "read book",
                new Date(),
                new Date(),
                Status.PENDING,
                Priority.HIGH,
                "new comments",
                null,
                "admin@mail.ru"
        );
        taskRepository.save(task);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/get-task/{taskId}", task.getId())
                .contentType(MediaType.APPLICATION_JSON)).andDo(print());

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(task.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(task.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(task.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(task.getStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value(task.getPriority().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments").value(task.getComments()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.executor").value(task.getExecutor()))
        ;
    }


    @DisplayName("Добавление задачи с правами администратора")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whenPostRequestToTaskAndValidTask_thenCorrectResponse() throws Exception {
        MediaType applicationJson = new MediaType(MediaType.APPLICATION_JSON);
        String taskDto = "{\"title\":\"title100\",\"description\":\"read book\",\"status\":\"PENDING\",\"priority\":\"HIGH\",\"comments\":\"new comments\",\"executor\":\"admin@mail.ru\"}";
        mockMvc.perform(post("/api/v1/create-task")
                        .content(taskDto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(applicationJson));
    }


    @DisplayName("Добавление задачи с невалидным полем")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whenPostRequestToTaskAndValidTask_thenInvalidResponse() throws Exception {
        MediaType applicationJson = new MediaType(MediaType.APPLICATION_JSON);
        String task = "{\"title\":\"\",\"description\":\"read book\",\"status\":\"PENDING\",\"priority\":\"HIGH\",\"comments\":\"new comments\",\"executor\":\"admin@mail.ru\"}";
        mockMvc.perform(post("/api/v1/create-task")
                        .content(task)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(applicationJson));
    }


    @DisplayName("Удаление задачи")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whenDeleteRequestToTaskAndValidTask_thenValidResponse() throws Exception {
        MediaType applicationJson = new MediaType(MediaType.APPLICATION_JSON);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/delete-task/28")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(applicationJson));
    }

}
