package ru.sber.atm.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.sber.atm.model.Terminal;
import ru.sber.atm.model.request.AmountRequest;
import ru.sber.atm.model.request.PinRequest;
import ru.sber.atm.repository.TerminalRepository;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TerminalControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // Создаем контейнер с PostgreSQL
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")

            .withDatabaseName("test-db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        postgres.start();
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    void setup() {
        terminalRepository.deleteAll();
        terminalRepository.save(new Terminal(1L,"123456", "1234", 5000L, LocalDateTime.now(), LocalDateTime.now()));
    }

    @Test
    @Order(2)
    void validatePin_Success() throws Exception {
        PinRequest request = new PinRequest("123456", "1234");

        mockMvc.perform(post("/api/validate-pin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Доступ разрешен"));
    }

    @Test
    @Order(1)
    void validatePin_Incorrect() throws Exception {
        PinRequest request = new PinRequest("123456", "0000");

        mockMvc.perform(post("/api/validate-pin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Неверный PIN. Осталось попыток: 2"));
    }

    @Test
    @Order(3)
    void getBalance_Success() throws Exception {
        mockMvc.perform(get("/api/balance/123456"))
                .andExpect(status().isOk())
                .andExpect(content().string("5000"));
    }

    @Test
    @Order(4)
    void deposit_Success() throws Exception {
        AmountRequest request = new AmountRequest("123456", 1000);

        mockMvc.perform(post("/api/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Операция выполнена!"));

        mockMvc.perform(get("/api/balance/123456"))
                .andExpect(status().isOk())
                .andExpect(content().string("6000"));
    }

    @Test
    @Order(5)
    void withdraw_Success() throws Exception {
        AmountRequest request = new AmountRequest("123456", 2000);

        mockMvc.perform(post("/api/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Операция выполнена!"));

        mockMvc.perform(get("/api/balance/123456"))
                .andExpect(status().isOk())
                .andExpect(content().string("4000"));
    }

    @Test
    @Order(6)
    void withdraw_InsufficientFunds() throws Exception {
        AmountRequest request = new AmountRequest("123456", 10000);

        mockMvc.perform(post("/api/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Недостаточно средств. Баланс: 4000"));
    }

    @Test
    @Order(7)
    void withdraw_NotMultipleOf100() throws Exception {
        AmountRequest request = new AmountRequest("123456", 350);

        mockMvc.perform(post("/api/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()                        )
                .andExpect(content().string("Сумма должна быть кратна 100."));
    }
}

