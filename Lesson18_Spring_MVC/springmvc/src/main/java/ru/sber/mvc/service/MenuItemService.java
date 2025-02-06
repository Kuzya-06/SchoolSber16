package ru.sber.mvc.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sber.mvc.mapper.MenuItemMapper;
import ru.sber.mvc.model.dto.MenuItemDto;
import ru.sber.mvc.model.entity.MenuItem;
import ru.sber.mvc.repository.MenuItemRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private static final Logger log = LoggerFactory.getLogger(MenuItemService.class);
    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper mapper;

    @Transactional(readOnly = true)
    public Page<MenuItemDto> getAll(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<MenuItem> menuItems = menuItemRepository.findAll(pageable);

        // Преобразуем список MenuItem -> MenuItemDto
        List<MenuItemDto> dtoList = menuItems.getContent()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        // Создаем новый Page<MenuItemDto>
        return new PageImpl<>(dtoList, pageable, menuItems.getTotalElements());
    }

    @Transactional(readOnly = true)
    public MenuItemDto getById(Long id) {
        return menuItemRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> {
                    log.debug("Продукт с id {} не найдено", id);
                    return new EntityNotFoundException("Продукт с id " + id + " " + "не найден");
                });
    }

    @Transactional()
    public void save(MenuItemDto menuItemDto) {
        MenuItem menuItem;

        if (menuItemDto.getId() != null) {
            // Если ID не null, значит, это обновление — получаем существующий объект из базы
            menuItem = menuItemRepository.findById(menuItemDto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Продукт не найден"));

            // Обновляем поля, но оставляем старый createDate
            menuItem.setName(menuItemDto.getName());
            menuItem.setPrice(menuItemDto.getPrice());
            menuItem.setDescription(menuItemDto.getDescription());
        } else {
            // Если ID null, значит, это новый продукт
            menuItem = mapper.toEntity(menuItemDto);
        }

        menuItemRepository.save(menuItem);
    }

    @Transactional()
    public void delete(Long id) {
        menuItemRepository.deleteById(id);
    }
}
