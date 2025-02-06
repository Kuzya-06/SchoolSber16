package ru.sber.mvc.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.sber.mvc.model.dto.MenuItemDto;
import ru.sber.mvc.service.MenuItemService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MenuController.class)
public class MenuControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuItemService menuItemService;

    @Test
    void testShowEditForm_Success() throws Exception {
        MenuItemDto menuItemDto = new MenuItemDto(1L, "Суши", 700.0, "Вкусные суши");
        when(menuItemService.getById(1L)).thenReturn(menuItemDto);

        mockMvc.perform(get("/menu/1/edit")).andExpect(status().isOk()).andExpect(view().name("menu/form")).andExpect(model().attributeExists("menuItem")).andExpect(model().attribute("menuItem", menuItemDto));
    }

    @Test
    void testShowEditForm_NotFound() throws Exception {
        when(menuItemService.getById(999L)).thenThrow(new EntityNotFoundException("Продукт с id " + 999 + " " + "не " + "найден"));

        mockMvc.perform(get("/menu/999/edit")).andExpect(view().name("redirect:/menu"));
    }

    @Test
    void testListMenu_Success() throws Exception {
        Page<MenuItemDto> menuPage = new PageImpl<>(List.of(new MenuItemDto(1L, "Пицца", 500.0, "Вкусная пицца")));
        System.out.println(menuPage.stream().toList());
        when(menuItemService.getAll(0, 10, "name", "asc")).thenReturn(menuPage);

        mockMvc.perform(get("/menu")).andExpect(status().isOk()).andExpect(view().name("menu/list")).andExpect(model().attributeExists("menuItems", "currentPage", "totalPages", "sortBy", "direction"));
    }

    @Test
    void testShowCreateForm_Success() throws Exception {
        mockMvc.perform(get("/menu/new")).andExpect(status().isOk()).andExpect(view().name("menu/form")).andExpect(model().attributeExists("menuItem", "page", "sortBy", "direction"));
    }

    @Test
    void testDelete_Success() throws Exception {
        doNothing().when(menuItemService).delete(1L);

        mockMvc.perform(get("/menu/1/delete")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/menu?page=0&sortBy=name&direction=asc"));

        verify(menuItemService).delete(1L);
    }

        @Test
        void testCreateOrUpdate_Valid_WhenMenuItemExists() throws Exception {
            MenuItemDto menuItemDto = new MenuItemDto(1L, "Burger", 250, "Burger best");
            doNothing().when(menuItemService).save(menuItemDto);

            mockMvc.perform(post("/menu")
                            .flashAttr("menuItem", menuItemDto))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/menu?page=0&sortBy=name&direction=asc"));

            verify(menuItemService, times(1)).save(menuItemDto);
        }

    @Test
    void testCreateOrUpdate_Valid_WhenMenuItemNew() throws Exception {
        // Данные для обновления
        MenuItemDto updatedMenuItem = new MenuItemDto(null, "Updated Burger", 300, "Updated description");

        doNothing().when(menuItemService).save(updatedMenuItem);

        mockMvc.perform(post("/menu")
                        .flashAttr("menuItem", updatedMenuItem))
                .andExpect(status().is3xxRedirection())  // Ожидаем редирект
                .andExpect(redirectedUrl("/menu?page=0&sortBy=name&direction=asc"));

        verify(menuItemService, times(1)).save(updatedMenuItem);
    }


    @Test
    void testCreateOrUpdate_Invalid_WhenIncorrectObject() throws Exception {
        // Некорректный объект (пусть пустое название)
        MenuItemDto invalidMenuItem = new MenuItemDto(1L, "", 0, "");

        mockMvc.perform(post("/menu")
                        .flashAttr("menuItem", invalidMenuItem))
                .andExpect(status().isOk())  // Должен остаться на той же странице
                .andExpect(view().name("menu/form"))  // Должен вернуть форму
                .andExpect(model().attributeExists("menuItem", "page", "sortBy", "direction"))
                .andExpect(model().attributeHasFieldErrors("menuItem", "name", "price", "description"));

        verify(menuItemService, never()).save(any());  // Убеждаемся, что сохранение не вызвано
    }

}
