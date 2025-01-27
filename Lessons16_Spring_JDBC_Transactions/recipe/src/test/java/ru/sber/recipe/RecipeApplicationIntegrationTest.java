package ru.sber.recipe;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.sber.recipe.service.RecipeManager;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@SpringBootTest(classes = {RecipeManager.class, RecipeApplication.class} )
class RecipeApplicationCLITest {
    private JdbcTemplate jdbcTemplate;
    private RecipeManager recipeManager;

    @BeforeEach
    void setUp() {
        initDatabase();
        initMocks();
        initTables();
    }

    @Test
    @Order(1)
    @Timeout(10)
    void testAddRecipe_isValid() {
        // Arrange: подготовка ввода для симуляции CLI
        String simulatedInput = """
                1                
                Test Recipe
                Water-1, Sugar-2
                0
                """;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Act: запускаем приложение
        RecipeApplication.main(new String[]{});

        // Assert: проверяем, что рецепт добавлен
        List<String> recipes = recipeManager.searchRecipeByName("Test Recipe");
        recipeManager.deleteByNameRecipe("Test Recipe");
        assertEquals(1, recipes.size());
        assertTrue(recipes.get(0).contains("Test Recipe"));
    }

    @Test
    @Order(2)
    @Timeout(10)
    void testSearchRecipeByName_isValid() {
        // Arrange: добавляем тестовый рецепт
        String recipeName = "Search Test Recipe";
        List<String> ingredients = List.of("Salt-1", "Flour-2");
        recipeManager.addRecipe(recipeName, ingredients);

        String simulatedInput = """
                2
                Search Test
                0
                """;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Act: запускаем приложение
        RecipeApplication.main(new String[]{});

        // Assert: проверяем вывод
        List<String> foundRecipes = recipeManager.searchRecipeByName("Search Test");
        recipeManager.deleteByNameRecipe("Search Test Recipe");
        assertFalse(foundRecipes.isEmpty());
        assertTrue(foundRecipes.get(0).contains(recipeName));
    }

    @Test
    @Order(3)
    @Timeout(10)
    void testDeleteByIdRecipe_isValid() {
        // Arrange: добавляем тестовый рецепт
        String recipeName = "Delete Test Recipe";
        List<String> ingredients = List.of("Milk-1", "Honey-2");
        int recipeId = recipeManager.addRecipe(recipeName, ingredients);

        String simulatedInput = String.format("""
                3
                %d
                0
                """, recipeId);
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Act: запускаем приложение
        RecipeApplication.main(new String[]{});

        // Assert: проверяем, что рецепт удален
        List<String> recipes = recipeManager.searchRecipeByName(recipeName);
        assertTrue(recipes.isEmpty());
    }

    @Test
    @Order(4)
    @Timeout(10)
    void testDeleteByNameRecipe_isValid() {
        // Arrange: добавляем тестовый рецепт
        String recipeName = "Delete Test Recipe";
        List<String> ingredients = List.of("Milk-1", "Honey-2");
        recipeManager.addRecipe(recipeName, ingredients);

        String simulatedInput = String.format("""
                4
                %s
                0
                """, recipeName);
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Act: запускаем приложение
        RecipeApplication.main(new String[]{});

        // Assert: проверяем, что рецепт удален
        List<String> recipes = recipeManager.searchRecipeByName(recipeName);
        assertTrue(recipes.isEmpty());
    }

    @Test
    @Order(6)
    @Timeout(10)
    @DisplayName("Проверяет, что пустое имя рецепта повторяет ввод имя рецепта")
    void testAddRecipe_isValid_NameOfTheRecipeMustBeIntroduced() {
        // Arrange: подготовка ввода для симуляции CLI
        String simulatedInput = """
                1    
                                
                            
                Test Recipe
                Water-1, Sugar-2
                0
                """;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Act: запускаем приложение
        RecipeApplication.main(new String[]{});

        // Assert: проверяем, что рецепт добавлен
        List<String> recipes = recipeManager.searchRecipeByName("Test Recipe");
        recipeManager.deleteByNameRecipe("Test Recipe");
        assertEquals(1, recipes.size());
        assertTrue(recipes.get(0).contains("Test Recipe"));
    }

    private void initDatabase() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:h2:mem:testdb");
        dataSource.setUsername("test");
        dataSource.setPassword("test");
        dataSource.setDriverClassName("org.h2.Driver");

        jdbcTemplate = new JdbcTemplate(dataSource);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        recipeManager = new RecipeManager(jdbcTemplate, namedParameterJdbcTemplate);
    }

    private void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    private void initTables() {
        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS recipes (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL
                );
                                
                CREATE TABLE IF NOT EXISTS ingredients (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    recipe_id INT,
                    name VARCHAR(255) NOT NULL,
                    quantity VARCHAR(50),
                    FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
                );
                """);
    }
}
