package com.userservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.dto.UserDto;
import com.userservice.entity.User;
import com.userservice.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID userTestId;
    private String userEmail;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setName("TestName");
        user.setSurname("TestSurname");
        user.setEmail("test@test.com");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        User savedUser = usersRepository.save(user);
        userTestId = savedUser.getId();
        userEmail = savedUser.getEmail();
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        mockMvc.perform(get("/api/user/email/")
                        .param("email", userEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(userEmail));
    }

    @Test
    public void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/user/" + userTestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userTestId.toString()))
                .andExpect(jsonPath("$.name").value("TestName"))
                .andExpect(jsonPath("$.surname").value("TestSurname"))
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    public void testCreateUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("John");
        userDto.setSurname("Wick");
        userDto.setEmail("johnwick@gmail.com");
        userDto.setBirthDate(LocalDate.of(1991, 6, 30));

        String jsonContent = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(userTestId);
        userDto.setName("Updated name");
        userDto.setSurname("Updated surname");

        String jsonContent = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/user/" + userTestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated name"))
                .andExpect(jsonPath("$.surname").value("Updated surname"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/user/" + userTestId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/user/" + userTestId))
                .andExpect(status().isNotFound());
    }
}
