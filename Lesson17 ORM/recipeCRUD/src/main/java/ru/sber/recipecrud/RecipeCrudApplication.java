package ru.sber.recipecrud;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.sber.recipecrud.exceptions.InvalidInputException;
import ru.sber.recipecrud.model.Recipe;
import ru.sber.recipecrud.service.RecipeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class RecipeCrudApplication implements CommandLineRunner {
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";   // Сброс цвета

    private final RecipeService recipeService;

    public RecipeCrudApplication(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public static void main(String[] args) {
        SpringApplication.run(RecipeCrudApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        try {
            Scanner scanner = new Scanner(System.in);

            while (true) {

            String chooseTheAction = """
                    Выберите действие: 
                    1 - Добавить рецепт, 
                    2 - Поиск рецепта по имени, 
                    3 - Удалить рецепт по ID, 
                    4 - Удалить рецепт по наименованию, 
                    5 - Поиск рецепта по ингредиенту,
                    0 - Выход
                    """;
            System.out.print(GREEN + chooseTheAction + RESET);
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> {
                    System.out.print(BLUE + "Введите имя рецепта: " + RESET);
                    String name = scanner.nextLine();
                    System.out.println("Вы ввели " + name);
                    while (name.isEmpty()) {
                        System.out.print(RED + "Должно быть введено наименование рецепта \n" + RESET);
                        name = scanner.nextLine();
                    }
                    String enterTheIngredients = """
                            Введите ингредиенты (имя-количество), разделенные запятыми: \\n
                            water-100, flour-400, sugar-200
                            """;
                    System.out.print(BLUE + enterTheIngredients + RESET);
                    String ingredientsInput = scanner.nextLine();
                    System.out.println("Вы ввели " + ingredientsInput);
                    while (ingredientsInput.isEmpty()) {
                        System.out.print(RED + "Должны быть введены ингредиенты \n" + enterTheIngredients + RESET);
                        ingredientsInput = scanner.nextLine();
                    }
                    List<String> ingredients = validateIngredients(ingredientsInput);
                    Recipe recipe = recipeService.addRecipe(name, ingredients);
                    System.out.println(BLUE + "Рецепт добавлен: " + recipe.getId() + RESET);
                }
                case 2 -> {
                    System.out.print(BLUE + "Введите имя или часть имени рецепта: " + RESET);
                    String searchName = scanner.nextLine();
                    List<Recipe> foundRecipes = recipeService.searchRecipeByName(searchName);
                    foundRecipes.forEach(recipe ->
                            System.out.println(recipe.getName() + " (ID=" + recipe.getId() + ")"+recipe.getIngredients()));
                }
                case 3 -> {
                    System.out.print(BLUE + "Введите ID рецепта для удаления: " + RESET);
                    int idToDelete = Integer.parseInt(scanner.nextLine());
                    System.out.println("Вы ввели " + idToDelete);
                    boolean deleted = recipeService.deleteRecipeById(idToDelete);
                    System.out.println(deleted ? "Рецепт удален" : "Рецепт не найден");
                }
                case 4 -> {
                    System.out.print(BLUE + "Введите наименование рецепта для удаления: " + RESET);
                    String nameToDelete = scanner.nextLine();
                    boolean deleted = recipeService.deleteRecipeByName(nameToDelete);
                    System.out.println(deleted ? "Рецепт удален" : "Рецепт не найден");
                }
                case 5 -> {
                    System.out.print(BLUE + "Введите наименование ингредиента для поиска рецептов: " + RESET);
                    String searchName = scanner.nextLine();
                    List<Recipe> foundRecipes = recipeService.searchByIngredientName(searchName);
                    if (foundRecipes.isEmpty()) {
                        System.out.println(RED + "Рецепты не найдены." + RESET);
                    } else {
                        System.out.println("Найденные рецепты:");
                        for (Recipe recipe : foundRecipes) {
                            System.out.println(recipe.getName() + " (" + recipe.getId() + ")");
                        }
                    }
                }
                case 0 -> {
                    System.out.println("Выход из приложения.");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Неверный выбор, попробуйте снова.");
            }
            }
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
