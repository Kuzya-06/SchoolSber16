package ru.sber.mvc.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.sber.mvc.mapper.MenuItemMapper;
import ru.sber.mvc.model.dto.MenuItemDto;
import ru.sber.mvc.model.entity.MenuItem;
import ru.sber.mvc.repository.MenuItemRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private MenuItemMapper mapper;

    @InjectMocks
    private MenuItemService menuItemService;

    private MenuItem menuItem;
    private MenuItemDto menuItemDto;

    @BeforeEach
    void setUp() {
        menuItem = new MenuItem(1L, "Суши", 700.0, "Вкусные суши", null, null);
        menuItemDto = new MenuItemDto(1L, "Суши", 700.0, "Вкусные суши");
    }

    @Test
    void testGetAll_Success_WhenEntityExists() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<MenuItem> menuItemsPage = new PageImpl<>(List.of(menuItem), pageable, 1);

        when(menuItemRepository.findAll(pageable)).thenReturn(menuItemsPage);
        when(mapper.toDto(menuItem)).thenReturn(menuItemDto);

        Page<MenuItemDto> result = menuItemService.getAll(0, 10, "name", "asc");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Суши", result.getContent().get(0).getName());

        verify(menuItemRepository).findAll(pageable);
        verify(mapper).toDto(menuItem);
    }

    @Test
    void testGetById_Success_WhenEntityExists() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        when(mapper.toDto(menuItem)).thenReturn(menuItemDto);

        MenuItemDto result = menuItemService.getById(1L);

        assertNotNull(result);
        assertEquals("Суши", result.getName());
        assertEquals(700.0, result.getPrice());

        verify(menuItemRepository).findById(1L);
        verify(mapper).toDto(menuItem);
    }

    @Test
    void testGetById_NotFound_WhenEntityEmpty() {
        when(menuItemRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> menuItemService.getById(2L));
    }

    @Test
    void testSave_NewMenuItem_WhenMenuItemNew() {
        menuItemDto = new MenuItemDto(null, "Суши", 700.0, "Вкусные суши");
        when(mapper.toEntity(menuItemDto)).thenReturn(menuItem);
        when(menuItemRepository.save(menuItem)).thenReturn(menuItem);

        menuItemService.save(menuItemDto);

        verify(menuItemRepository).save(menuItem);
    }

    @Test
    void testSave_UpdateMenuItem_WhenMenuItemExists() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        menuItemDto.setName("Роллы");
        menuItemService.save(menuItemDto);

        assertEquals("Роллы", menuItem.getName());
        verify(menuItemRepository).save(menuItem);
    }

    @Test
    void testSave_ThrowsEntityNotFoundException_WhenMenuItemNotFound() {
        MenuItemDto menuItemDto = new MenuItemDto(1L, "Суши", 700.0, "Вкусный суши");

        // Имитируем, что элемент с таким ID отсутствует в базе
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        // Проверяем, что метод save выбросит EntityNotFoundException
        assertThrows(EntityNotFoundException.class, () -> menuItemService.save(menuItemDto));

        // Проверяем, что вызов findById действительно произошёл
        verify(menuItemRepository, times(1)).findById(1L);

        // Проверяем, что menuItemRepository.save() не вызывался
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void testDelete_Success_ById() {
        menuItemService.delete(1L);
        verify(menuItemRepository).deleteById(1L);
    }
}
