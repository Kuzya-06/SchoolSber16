package ru.sber.recipe.command;

import ru.sber.recipe.service.RecipeManager;

import java.util.Scanner;

public class DeleteRecipeByIdCommand implements Command {
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String RESET = "\u001B[0m";   // Сброс цвета

    private final RecipeManager recipeManager;
    private final Scanner scanner;


    public DeleteRecipeByIdCommand(RecipeManager recipeManager, Scanner scanner) {
        this.recipeManager = recipeManager;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print(BLUE + "Введите ID рецепта для удаления: " + RESET);
        String idToDelete = scanner.nextLine();
        if (recipeManager.deleteByIdRecipe(Integer.parseInt(idToDelete)) == 1)
            System.out.println("Рецепт c ID " + idToDelete + " удален!");
        else System.out.println("Рецепта с таким ID нет!");
    }
}
