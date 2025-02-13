package ru.sber.recipe.command;

import ru.sber.recipe.service.RecipeManager;

import java.util.Scanner;

public class DeleteRecipeByNameCommand implements Command {

    private static final String BLUE = "\u001B[34m";
    private static final String RESET = "\u001B[0m";   // Сброс цвета


    private final RecipeManager recipeManager;
    private final Scanner scanner;


    public DeleteRecipeByNameCommand(RecipeManager recipeManager, Scanner scanner) {
        this.recipeManager = recipeManager;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print(BLUE + "Введите наименование рецепта для удаления: " + RESET);
        String nameToDelete = scanner.nextLine();
        if (recipeManager.deleteByNameRecipe(nameToDelete) == 1)
            System.out.println("Рецепт " + nameToDelete + " удален!");
        else System.out.println("Рецепта с таким наименованием нет!");

    }
}
