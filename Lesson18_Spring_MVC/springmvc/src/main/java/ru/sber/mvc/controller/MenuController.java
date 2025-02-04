package ru.sber.mvc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.RequestScope;
import ru.sber.mvc.model.dto.MenuItemDto;
import ru.sber.mvc.service.MenuItemService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/menu")
@Slf4j
@RequestScope
public class MenuController {

    private final MenuItemService menuItemService;

    @GetMapping
    public String listMenu(Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "name") String sortBy,
                           @RequestParam(defaultValue = "asc") String direction) {
        Page<MenuItemDto> menuPage = menuItemService.getAll(page, 10, sortBy, direction);
        log.debug("menuPage = {}", menuPage==null? "menuPage":menuPage.stream().toList());
        log.debug("menuPage.getContent() = {}", menuPage==null? "menuPage":menuPage.getContent().stream().toList());
        log.debug("menuPage.getTotalPages() = {}", menuPage==null? "menuPage":menuPage.getTotalPages());

        assert menuPage != null;
        model.addAttribute("menuItems", menuPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", menuPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        return "menu/list";
    }

    // Кнопка Добавить
    @GetMapping("/new")
    public String showCreateForm(Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "name") String sortBy,
                                 @RequestParam(defaultValue = "asc") String direction) {
        model.addAttribute("menuItem", new MenuItemDto());
        model.addAttribute("page", page);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        return "menu/form";
    }

    @PostMapping
    public String createOrUpdate(
            @Valid @ModelAttribute("menuItem") MenuItemDto menuItemDto,
            BindingResult result,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model
    ) {
        log.info("Ошибки валидации: {}", result.getAllErrors());

        if (result.hasErrors()) {
//            model.addAttribute("menuItem", new MenuItemDto());
            model.addAttribute("page", page);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("direction", direction);
            return "menu/form";
        }

        menuItemService.save(menuItemDto);
        return "redirect:/menu?page=" + page + "&sortBy=" + sortBy + "&direction=" + direction;
    }

    // Кнопка Редактировать
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "name") String sortBy,
                               @RequestParam(defaultValue = "asc") String direction) {
        model.addAttribute("menuItem", menuItemService.getById(id).orElseThrow());
        model.addAttribute("page", page);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        return "menu/form";
    }

    // Кнопка удалить
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "name") String sortBy,
                         @RequestParam(defaultValue = "asc") String direction) {
        menuItemService.delete(id);
        return "redirect:/menu?page=" + page + "&sortBy=" + sortBy + "&direction=" + direction;
    }
}
