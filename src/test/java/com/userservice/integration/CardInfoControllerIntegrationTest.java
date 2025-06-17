package com.userservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.dto.CardInfoDto;
import com.userservice.entity.CardInfo;
import com.userservice.entity.Users;
import com.userservice.repository.CardInfoRepository;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class CardInfoControllerIntegrationTest {
    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CardInfoRepository cardInfoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsersRepository usersRepository;

    private UUID cardInfoTestId;
    private UUID userTestId;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    public void setUp() {
        Users user = new Users();
        user.setName("TestName");
        user.setSurname("TestSurname");
        user.setEmail("test@test.com");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        Users savedUser = usersRepository.save(user);
        userTestId = savedUser.getId();

        CardInfo cardInfo = new CardInfo();
        cardInfo.setHolder("Test Holder");
        cardInfo.setNumber("1111111111111111");
        cardInfo.setUser(savedUser);
        cardInfo.setExpirationDate(LocalDate.of(2025, 6, 16));
        CardInfo savedCardInfo = cardInfoRepository.save(cardInfo);
        cardInfoTestId = savedCardInfo.getId();
    }

    @Test
    public void testGetCardInfoById() throws Exception {
        mockMvc.perform(get("/api/card_infos/" + cardInfoTestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cardInfoTestId.toString()))
                .andExpect(jsonPath("$.holder").value("Test Holder"));
    }

    @Test
    public void testCreateCardInfo() throws Exception {
        CardInfoDto cardInfoDto = new CardInfoDto();
        cardInfoDto.setUserId(userTestId);
        cardInfoDto.setHolder("Name Surname");
        cardInfoDto.setNumber("2222222222222222");
        cardInfoDto.setExpirationDate(LocalDate.of(2026, 6, 30));

        String jsonContent = objectMapper.writeValueAsString(cardInfoDto);

        mockMvc.perform(post("/api/card_infos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateCardInfo() throws Exception {
        CardInfoDto updateDto = new CardInfoDto();
        updateDto.setId(cardInfoTestId);
        updateDto.setHolder("Updated Holder");
        updateDto.setNumber("1111111111111111");
        updateDto.setExpirationDate(LocalDate.of(2026, 12, 31));

        String jsonContent = objectMapper.writeValueAsString(updateDto);

        mockMvc.perform(put("/api/card_infos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/card_infos/" + cardInfoTestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.holder").value("Updated Holder"))
                .andExpect(jsonPath("$.number").value("1111111111111111"));
    }

    @Test
    public void testDeleteCardInfo() throws Exception {
        mockMvc.perform(delete("/api/card_infos/" + cardInfoTestId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/card_infos/" + cardInfoTestId))
                .andExpect(status().isNotFound());
    }
}
