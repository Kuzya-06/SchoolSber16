package ru.sber.recipe;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.sber.recipe.exceptions.InvalidInputException;
import ru.sber.recipe.service.RecipeManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
@AllArgsConstructor
public class RecipeApplication implements CommandLineRunner {

    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";   // Сброс цвета
    private RecipeManager recipeManager;

    public static void main(String[] args) {
        SpringApplication.run(RecipeApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try{
        Scanner scanner = new Scanner(System.in);
        String ChooseTheAction = """
                Выберите действие: 
                1 - Добавить рецепт, 
                2 - Поиск рецепта по имени, 
                3 - Удалить рецепт по ID, 
                4 - Удалить рецепт по наименованию, 
                5 - Поиск рецепта по ингредиенту,
                0 - Выход
                """;
        String EnterTheIngredients = """
                Введите ингредиенты (имя-количество), разделенные запятыми: \\n
                water-100, flour-400, sugar-200
                """;
        //Для тестов закомментирован while
//        while (true) {
        System.out.print(GREEN + ChooseTheAction + RESET);
        int choice = Integer.parseInt(scanner.nextLine());
//        String s = scanner.nextLine();// consume newline

        switch (choice) {
            case 1 -> {
                System.out.print(BLUE + "Введите имя рецепта: " + RESET);
                String name = scanner.nextLine();
                while (name.isEmpty()) {
                    System.out.print(RED + "Должно быть введено наименование рецепта \n" + RESET);
                    name = scanner.nextLine();
                }
                String ingredientsInput = scanner.nextLine();
                while (ingredientsInput.isEmpty()) {
                    System.out.print(RED + "Должны быть введены ингредиенты \n" + EnterTheIngredients + RESET);
                    ingredientsInput = scanner.nextLine();
                }
                List<String> ingredients = validateIngredients(ingredientsInput);
                System.out.println("List<String> ingredients = "+ingredients);
                int i = recipeManager.addRecipe(name, ingredients);
                System.out.println(BLUE + "Рецепт добавлен! " + i + RESET);
            }
            case 2 -> {
                System.out.print(BLUE + "Введите имя или часть имени рецепта: " + RESET);
                String searchName = scanner.nextLine();
                List<String> foundRecipes = recipeManager.searchRecipeByName(searchName);
                if (foundRecipes.isEmpty()) {
                    System.out.println("Рецепты не найдены.");
                } else {
                    System.out.println(BLUE + "Найденные рецепты:" + RESET);
                    for (String recipe : foundRecipes) {
                        System.out.println(recipe);
                    }
                }
            }
            case 3 -> {
                System.out.print(BLUE + "Введите ID рецепта для удаления: " + RESET);
                int idToDelete = scanner.nextInt();
                if (recipeManager.deleteByIdRecipe(idToDelete) == 1)
                    System.out.println("Рецепт c ID " + idToDelete + " удален!");
                else System.out.println("Рецепта с таким ID нет!");
            }
            case 4 -> {
                System.out.print(BLUE + "Введите наименование рецепта для удаления: " + RESET);
                String nameToDelete = scanner.nextLine();
                if (recipeManager.deleteByNameRecipe(nameToDelete) == 1)
                    System.out.println("Рецепт " + nameToDelete + " удален!");
                else System.out.println("Рецепта с таким наименованием нет!");
            }
            case 5 -> {
                System.out.print(BLUE + "Введите наименование ингредиента для поиска рецептов: " + RESET);
                String searchName = scanner.nextLine();
                List<String> foundRecipes = recipeManager.searchByIngredientName(searchName);
                if (foundRecipes.isEmpty()) {
                    System.out.println(RED+"Рецепты не найдены."+RESET);
                } else {
                    System.out.println("Найденные рецепты:");
                    for (String recipe : foundRecipes) {
                        System.out.println(recipe);
                    }
                }
            }
            case 0 -> {
                System.out.println("Выход из приложения.");
                scanner.close();
            }
            default -> System.out.println("Неверный выбор. Попробуйте снова.");
        }
//        }
        } catch (InvalidInputException e) {
            System.err.println(RED + "Ошибка: " + e.getMessage() + RESET);
        } catch (Exception e) {
            System.err.println(RED + "Непредвиденная ошибка: " + e.getMessage() + RESET);
        }
    }

    private static List<String> validateIngredients(String ingredientsInput) {
        String[] ingredientsArray = new String[1];
        if (ingredientsInput.contains(",")) {
            ingredientsArray = ingredientsInput.split(",");
        } else {
            ingredientsArray[0] = ingredientsInput;
        }

        List<String> ingredients = new ArrayList<>();
        for (String ingredient : ingredientsArray) {
            ingredients.add(ingredient.trim());
        }
        return ingredients;
    }

}
