package ru.sber.recipe.command;

import ru.sber.recipe.service.RecipeManager;

import java.util.List;
import java.util.Scanner;

public class SearchRecipeByNameCommand implements Command {
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";   // Сброс цвета

    private final RecipeManager recipeManager;
    private final Scanner scanner;

    public SearchRecipeByNameCommand(RecipeManager recipeManager, Scanner scanner) {
        this.recipeManager = recipeManager;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print(BLUE + "Введите имя или часть имени рецепта: " + RESET);
        String searchName = scanner.nextLine();
        List<String> foundRecipes = recipeManager.searchRecipeByName(searchName);
        if (foundRecipes.isEmpty()) {
            System.out.println("Рецепты не найдены.");
        } else {
            System.out.println(BLUE + "Найденные рецепты:" + RESET);
            for (String recipe : foundRecipes) {
                System.out.println(BLUE + recipe + RESET);
            }
        }
    }

}
