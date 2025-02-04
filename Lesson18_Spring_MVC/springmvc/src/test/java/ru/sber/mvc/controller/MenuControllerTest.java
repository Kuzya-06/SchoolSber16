package ru.sber.mvc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.sber.mvc.model.dto.MenuItemDto;
import ru.sber.mvc.model.entity.MenuItem;
import ru.sber.mvc.service.MenuItemService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MenuController.class)
public class MenuControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MenuItemService menuItemService;

    @InjectMocks
    private MenuController menuController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(menuController).build();
    }

    @Test
    void testListMenu() throws Exception {
        Page<MenuItemDto> menuPage = new PageImpl<>(List.of(new MenuItemDto(1L, "Пицца", 500.0, "Вкусная пицца")));
        System.out.println(menuPage.stream().toList());
        when(menuItemService.getAll(0, 10, "name", "asc")).thenReturn(menuPage);

        mockMvc.perform(get("/menu"))
                .andExpect(status().isOk())
                .andExpect(view().name("menu/list"))
                .andExpect(model().attributeExists("menuItems", "currentPage", "totalPages", "sortBy", "direction"));
    }

    @Test
    void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/menu/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("menu/form"))
                .andExpect(model().attributeExists("menuItem", "page", "sortBy", "direction"));
    }

    @Test
    void testCreateOrUpdate_Valid() throws Exception {
        MenuItemDto menuItemDto = new MenuItemDto(1L, "Burger", 250, "Burger best");
        doNothing().when(menuItemService).save(menuItemDto);

        mockMvc.perform(post("/menu")
                        .flashAttr("menuItem", menuItemDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/menu?page=0&sortBy=name&direction=asc"));

        verify(menuItemService, times(1)).save(menuItemDto);
    }

    @Test
    void testShowEditForm() throws Exception {
        MenuItem menuItem = new MenuItem(1L, "суши", 700.0, "Вкусный суши", LocalDateTime.now(), LocalDateTime.now());
        when(menuItemService.getById(1L)).thenReturn(Optional.ofNullable(menuItem));

        mockMvc.perform(get("/menu/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("menu/form"))
                .andExpect(model().attributeExists("menuItem", "page", "sortBy", "direction"));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(get("/menu/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/menu?page=0&sortBy=name&direction=asc"));

        verify(menuItemService, times(1)).delete(1L);
    }
}
