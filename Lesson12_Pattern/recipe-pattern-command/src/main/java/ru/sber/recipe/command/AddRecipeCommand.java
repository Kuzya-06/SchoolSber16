package ru.sber.recipe.command;

import ru.sber.recipe.service.RecipeManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AddRecipeCommand implements Command {

    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String RESET = "\u001B[0m";   // Сброс цвета

    private static final String ENTERTHEINGREDIENTS = """
                Введите ингредиенты (имя-количество), разделенные запятыми: \\n
                water-100, flour-400, sugar-200
                """;

    private final RecipeManager recipeManager;
    private final Scanner scanner;

    public AddRecipeCommand(RecipeManager recipeManager, Scanner scanner) {
        this.recipeManager = recipeManager;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print(BLUE + "Введите имя рецепта: " + RESET);

        String name = scanner.nextLine();
        while (name.isEmpty()) {
            System.out.print(RED + "Должно быть введено наименование рецепта \n" + RESET);
            name = scanner.nextLine();
        }

        System.out.print("Введите ингредиенты (имя-количество), разделенные запятыми: \n");
        String ingredientsInput = scanner.nextLine();
        while (ingredientsInput.isEmpty()) {
            System.out.print(RED + "Должны быть введены ингредиенты \n" + ENTERTHEINGREDIENTS + RESET);
            ingredientsInput = scanner.nextLine();
        }

        List<String> ingredients = validateIngredients(ingredientsInput);
        int recipeId = recipeManager.addRecipe(name, ingredients);
        System.out.println(BLUE + "Рецепт добавлен! " + recipeId + RESET);
    }

    private List<String> validateIngredients(String ingredientsInput) {
        String[] ingredientsArray = ingredientsInput.contains(",")
                ? ingredientsInput.split(",")
                : new String[]{ingredientsInput};

        List<String> ingredients = new ArrayList<>();
        for (String ingredient : ingredientsArray) {
            ingredients.add(ingredient.trim());
        }
        return ingredients;
    }
}