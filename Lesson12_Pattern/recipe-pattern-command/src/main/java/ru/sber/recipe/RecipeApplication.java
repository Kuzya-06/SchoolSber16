package ru.sber.recipe;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.sber.recipe.command.AddRecipeCommand;
import ru.sber.recipe.command.CommandExecutor;
import ru.sber.recipe.command.DeleteRecipeByIdCommand;
import ru.sber.recipe.command.DeleteRecipeByNameCommand;
import ru.sber.recipe.command.SearchRecipeByIngredientCommand;
import ru.sber.recipe.command.SearchRecipeByNameCommand;
import ru.sber.recipe.exceptions.InvalidInputException;
import ru.sber.recipe.service.RecipeManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
@AllArgsConstructor
public class RecipeApplication implements CommandLineRunner {

    public static final String RED = "\u001B[31m";
    public static final String RESET = "\u001B[0m";   // Сброс цвета

    private RecipeManager recipeManager;

    public static void main(String[] args) {
        SpringApplication.run(RecipeApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            Scanner scanner = new Scanner(System.in);
            CommandExecutor executor = new CommandExecutor(scanner);

            // Регистрация команд
            executor.registerCommand(1, new AddRecipeCommand(recipeManager, scanner));
            executor.registerCommand(2, new SearchRecipeByNameCommand(recipeManager, scanner));
            executor.registerCommand(3, new DeleteRecipeByIdCommand(recipeManager, scanner));
            executor.registerCommand(4, new DeleteRecipeByNameCommand(recipeManager, scanner));
            executor.registerCommand(5, new SearchRecipeByIngredientCommand(recipeManager, scanner));

            while (true) {
                System.out.print("""
                Выберите действие:
                1 - Добавить рецепт,
                2 - Поиск рецепта по имени,
                3 - Удалить рецепт по ID,
                4 - Удалить рецепт по наименованию,
                5 - Поиск рецепта по ингредиенту,
                0 - Выход
                """);

                int choice = Integer.parseInt(scanner.nextLine());
                if (choice == 0) {
                    System.out.println("Выход из приложения.");
                    break;
                }

                executor.executeCommand(choice);
            }
        } catch (InvalidInputException e) {
            System.err.println(RED + "Ошибка: " + e.getMessage() + RESET);
        } catch (RuntimeException e){
            System.err.println(RED + e.getMessage() + RESET);
        }
    }

}
