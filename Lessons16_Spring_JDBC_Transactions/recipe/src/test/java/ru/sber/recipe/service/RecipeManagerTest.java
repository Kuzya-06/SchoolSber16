package ru.sber.recipe.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.sber.recipe.exceptions.InvalidInputException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RecipeManagerTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @InjectMocks
    private RecipeManager recipeManager;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void testAddRecipe_isValid() {
        String recipeName = "Test Recipe";
        List<String> ingredients = Arrays.asList("Salt-1", "Pepper-2");

        when(jdbcTemplate.update(anyString(), eq(recipeName))).thenReturn(1);
        when(jdbcTemplate.queryForList(anyString())).thenReturn(Collections.singletonList(Map.of("id", 1)));

        int recipeId = recipeManager.addRecipe(recipeName, ingredients);

        assertEquals(1, recipeId);
        verify(jdbcTemplate, times(1)).update(anyString(), eq(recipeName));
        verify(jdbcTemplate, times(1)).batchUpdate(eq("INSERT INTO ingredients (recipe_id, name, quantity) VALUES (?, ?, ?)"), any(BatchPreparedStatementSetter.class));
    }

    @Test
    void testSearchRecipeByName_isValid() {
        String searchName = "Test";

        when(jdbcTemplate.queryForList(anyString(), anyString())).thenReturn(List.of(
                Map.of("recipe_id", 1, "recipe_name", "Test Recipe", "ingredient_name", "Salt", "quantity", "1"),
                Map.of("recipe_id", 1, "recipe_name", "Test Recipe", "ingredient_name", "Pepper", "quantity", "2")
        ));

        List<String> result = recipeManager.searchRecipeByName(searchName);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).contains("Test Recipe"));
        assertTrue(result.get(0).contains("Salt-1"));
        assertTrue(result.get(0).contains("Pepper-2"));
        verify(jdbcTemplate, times(1)).queryForList(anyString(), anyString());
    }

    @Test
    void testSearchByIngredientName_isValid() {
        String ingredientName = "Salt";

        when(jdbcTemplate.queryForList(anyString(), anyString())).thenReturn(List.of(
                Map.of("recipe_id", 1)
        ));

        when(namedParameterJdbcTemplate.queryForList(anyString(), anyMap())).thenReturn(List.of(
                Map.of("id", 1, "name", "Test Recipe")
        ));

        List<String> result = recipeManager.searchByIngredientName(ingredientName);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Recipe (1)", result.get(0));
        verify(jdbcTemplate, times(1)).queryForList(anyString(), anyString());
        verify(namedParameterJdbcTemplate, times(1)).queryForList(anyString(), anyMap());
    }

    @Test
    void testDeleteByIdRecipe_isValid() {
        int recipeId = 1;

        when(jdbcTemplate.update(anyString(), eq(recipeId))).thenReturn(1);

        int result = recipeManager.deleteByIdRecipe(recipeId);

        assertEquals(1, result);
        verify(jdbcTemplate, times(1)).update(anyString(), eq(recipeId));
    }

    @Test
    void testDeleteByNameRecipe_isValid() {
        String recipeName = "Test Recipe";

        when(jdbcTemplate.update(anyString(), eq(recipeName))).thenReturn(1);

        int result = recipeManager.deleteByNameRecipe(recipeName);

        assertEquals(1, result);
        verify(jdbcTemplate, times(1)).update(anyString(), eq(recipeName));
    }


    @Test
    @DisplayName(value = "Проверяет, что пустое имя рецепта вызывает исключение")
    void testAddRecipe_withEmptyName() {
        List<String> ingredients = List.of("Water-100", "Sugar-200");
        assertThrows(InvalidInputException.class,
                () -> recipeManager.addRecipe("", ingredients));
    }

    @Test
    @DisplayName(value = "Проверяет, что null имя рецепта вызывает исключение")
    void testAddRecipe_withNullName() {
        List<String> ingredients = List.of("Water-100", "Sugar-200");
        assertThrows(InvalidInputException.class,
                () -> recipeManager.addRecipe(null, ingredients));
    }

    @Test
    @DisplayName(value = "Проверяет, что пустой список ингредиентов вызывает исключение")
    void testAddRecipe_withEmptyIngredients() {
        String recipeName = "Test Recipe";
        assertThrows(InvalidInputException.class,
                () -> recipeManager.addRecipe(recipeName, List.of()));
    }

    @Test
    @DisplayName(value = "Проверяет, что ингредиенты без разделителя - вызывают исключение")
    void testAddRecipe_withInvalidIngredientFormat() {
        String recipeName = "Test Recipe IngredientFormat";
        List<String> invalidIngredients = List.of("InvalidIngredient1, InvalidIngredient2");
        InvalidInputException exception = assertThrows(InvalidInputException.class,
                () -> recipeManager.addRecipe(recipeName, invalidIngredients));
        assertEquals("Ингредиент должен быть в формате 'Имя-Количество'. Некорректный: InvalidIngredient1, InvalidIngredient2",
                exception.getMessage());
    }
}
