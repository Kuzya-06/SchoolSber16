package ru.sber.mvc.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.testcontainers.containers.PostgreSQLContainer;
import ru.sber.mvc.model.dto.MenuItemDto;
import ru.sber.mvc.model.entity.MenuItem;
import ru.sber.mvc.repository.MenuItemRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(initializers = MenuControllerITTest.PostgreSQLContainerInitializer.class)
public class MenuControllerITTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        menuItemRepository.deleteAll();
    }

    @Test
    @Order(1)
    void testListMenu() throws Exception {
        menuItemRepository.save(new MenuItem(null, "Борщ", 250, "Вкусный борщ", LocalDateTime.now(), LocalDateTime.now()));

        mockMvc.perform(get("/menu"))
                .andExpect(status().isOk())
                .andExpect(view().name("menu/list"))
                .andExpect(model().attributeExists("menuItems"))
                .andExpect(model().attributeExists("currentPage", "totalPages", "sortBy", "direction"));
    }

    @Test
    @Order(2)
    void testCreateMenuItem() throws Exception {
        MenuItemDto menuItemDto = new MenuItemDto(null, "Пельмени", 300, "Домашние пельмени");

        mockMvc.perform(post("/menu")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", menuItemDto.getName())
                        .param("price", String.valueOf(menuItemDto.getPrice()))
                        .param("description", menuItemDto.getDescription()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/menu?page=*&sortBy=*&direction=*")
                );

        assertEquals(1, menuItemRepository.count());
    }

    @Test
    @Order(3)
    void testEditMenuItem() throws Exception {
        MenuItem menuItem = menuItemRepository.save(new MenuItem(null, "Котлета", 150, "Куриная котлета", LocalDateTime.now(), LocalDateTime.now()));

        mockMvc.perform(get("/menu/" + menuItem.getId() + "/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("menu/form"))
                .andExpect(model().attributeExists("menuItem"));
    }

    @Test
    @Order(4)
    void testDeleteMenuItem() throws Exception {
        MenuItem menuItem = menuItemRepository.save(new MenuItem(null, "Суп", 200, "Горячий суп", LocalDateTime.now(), LocalDateTime.now()));

        mockMvc.perform(get("/menu/" + menuItem.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/menu?page=*&sortBy=*&direction=*")
                );

        assertFalse(menuItemRepository.findById(menuItem.getId()).isPresent());
    }


    @TestConfiguration
    static class PostgreSQLContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:16")
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");

        static {
            POSTGRESQL_CONTAINER.start();
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + POSTGRESQL_CONTAINER.getJdbcUrl(),
                    "spring.datasource.username=" + POSTGRESQL_CONTAINER.getUsername(),
                    "spring.datasource.password=" + POSTGRESQL_CONTAINER.getPassword()
            ).applyTo(applicationContext.getEnvironment());
        }
    }
}
