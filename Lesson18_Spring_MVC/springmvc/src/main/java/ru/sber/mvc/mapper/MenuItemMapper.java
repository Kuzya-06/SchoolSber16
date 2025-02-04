package ru.sber.mvc.mapper;

import ru.sber.mvc.model.dto.MenuItemDto;
import ru.sber.mvc.model.entity.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuItemMapper {

    MenuItemMapper INSTANCE = Mappers.getMapper(MenuItemMapper.class);

    // Преобразование из Entity в DTO
    MenuItemDto toDto(MenuItem menuItem);

    // Преобразование из DTO в Entity
    MenuItem toEntity(MenuItemDto menuItemDto);
}

