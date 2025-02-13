package ru.sber.recipe.command;

import ru.sber.recipe.service.RecipeManager;

import java.util.List;
import java.util.Scanner;

public class SearchRecipeByIngredientCommand implements Command {
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String RESET = "\u001B[0m";   // Сброс цвета

    private final RecipeManager recipeManager;
    private final Scanner scanner;

    public SearchRecipeByIngredientCommand(RecipeManager recipeManager, Scanner scanner) {
        this.recipeManager = recipeManager;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print(BLUE + "Введите наименование ингредиента для поиска рецептов: " + RESET);
        String searchName = scanner.nextLine();
        List<String> foundRecipes = recipeManager.searchByIngredientName(searchName);
        if (foundRecipes.isEmpty()) {
            System.out.println(RED+"Рецепты не найдены."+RESET);
        } else {
            System.out.println("Найденные рецепты:");
            for (String recipe : foundRecipes) {
                System.out.println(BLUE + recipe+RESET);
            }
        }
    }

}
