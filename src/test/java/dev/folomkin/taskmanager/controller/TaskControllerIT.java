package dev.folomkin.taskmanager.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class TaskControllerIT {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
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
        mockMvc.perform(get("/api/v1/get-task/27")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @DisplayName("Добавление задачи с правами администратора")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whenPostRequestToTaskAndValidTask_thenCorrectResponse() throws Exception {
        MediaType applicationJson = new MediaType(MediaType.APPLICATION_JSON);
        String task = "{\"title\":\"title\",\"description\":\"read book\",\"status\":\"PENDING\",\"priority\":\"HIGH\",\"comments\":\"new comments\",\"executor\":\"admin@mail.ru\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/create-task")
                        .content(task)
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
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/create-task")
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
