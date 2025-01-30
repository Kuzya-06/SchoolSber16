package ru.sber.recipecrud.service;


import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.sber.recipecrud.model.Ingredient;
import ru.sber.recipecrud.model.Recipe;
import ru.sber.recipecrud.repository.RecipeRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RecipeServiceTest {

    @Mock
    private EntityManager entityManager;
    @Mock
    private TypedQuery<Recipe> typedQuery;
    @Mock
    private RecipeRepository recipeRepository;
    @InjectMocks
    private RecipeService recipeService;
    private Recipe recipe;

    @BeforeEach
    void setUp() {
        openMocks(this);
        recipeService = new RecipeService(recipeRepository,  entityManager);

        recipe = new Recipe();
        recipe.setId(1);
        recipe.setName("Chocolate Cake");

        Ingredient ingredient = new Ingredient();
        ingredient.setId(1);
        ingredient.setName("Sugar");
        ingredient.setRecipe(recipe);

        recipe.setIngredients(List.of(ingredient));
    }

    @Test
    @DisplayName("Добавление рецепта должно вернуть рецепт")
    void addRecipe_ShouldReturnRecipe() {
        // Arrange
        String name = "Тестовый рецепт";
        List<String> ingredients = List.of("Вода-100", "Сахар-20");

        Recipe recipe = new Recipe();
        recipe.setName(name);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("Вода");
        ingredient1.setQuantity("100");

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Сахар");
        ingredient2.setQuantity("20");

        recipe.setIngredients(List.of(ingredient1, ingredient2));

        // Мокируем поведение EntityManager
        doNothing().when(entityManager).persist(any(Recipe.class)); // Не нужно возвращать значение

        // Act
        Recipe result = recipeService.addRecipe(name, ingredients);

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(2, result.getIngredients().size());
        verify(entityManager, times(1)).persist(any(Recipe.class)); // Проверяем, что persist был вызван
    }

    @Test
    @DisplayName("Удаление рецепта по ID должен вернуть True, когда существует рецепт")
    void deleteRecipeById_ShouldReturnTrue_WhenRecipeExists() {
        // Arrange
        int recipeId = 1;
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        when(entityManager.find(Recipe.class, recipeId)).thenReturn(recipe);

        // Act
        boolean result = recipeService.deleteRecipeById(recipeId);

        // Assert
        assertTrue(result);
        verify(entityManager, times(1)).remove(recipe); // Проверяем, что удаление было вызвано
    }

    @Test
    @DisplayName("Удаление рецепта с помощью ID должен вернуть false, когда рецепт не существует")
    void deleteRecipeById_ShouldReturnFalse_WhenRecipeDoesNotExist() {
        // Arrange
        int recipeId = 1;
        when(entityManager.find(Recipe.class, recipeId)).thenReturn(null);

        // Act
        boolean result = recipeService.deleteRecipeById(recipeId);

        // Assert
        assertFalse(result);
        verify(entityManager, never()).remove(any(Recipe.class)); // Удаление не должно быть вызвано
    }

    @Test
    @DisplayName("Удаление рецепта по имени должно вернуть True, когда существует рецепт")
    void deleteRecipeByName_ShouldReturnTrue_WhenRecipeExists() {
        // Arrange
        String recipeName = "Тестовый рецепт";
        Recipe recipe = new Recipe();
        recipe.setName(recipeName);

        when(recipeRepository.findByNameIgnoreCase(recipeName)).thenReturn(List.of(recipe));

        // Act
        boolean result = recipeService.deleteRecipeByName(recipeName);

        // Assert
        assertTrue(result);
        verify(entityManager, times(1)).remove(recipe); // Проверяем, что удаление было вызвано
    }

    @Test
    @DisplayName("Удаление рецепта по имени должно вернуть false, когда рецепт не существует")
    void deleteRecipeByName_ShouldReturnFalse_WhenRecipeDoesNotExist() {
        // Arrange
        String recipeName = "Несущественный рецепт";
        when(recipeRepository.findByNameIgnoreCase(recipeName)).thenReturn(List.of());

        // Act
        boolean result = recipeService.deleteRecipeByName(recipeName);

        // Assert
        assertFalse(result);
        verify(entityManager, never()).remove(any(Recipe.class)); // Удаление не должно быть вызвано
    }

    @Test
    @DisplayName("Рецепт поиска по имени должен вернуть список, когда найдены рецепты")
    void searchRecipeByName_ShouldReturnList_WhenRecipesFound() {

        String searchName = "Cake";

        when(entityManager.createQuery(anyString(), eq(Recipe.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("namePart", searchName)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(recipe));

        // Act
        List<Recipe> result = recipeService.searchRecipeByName(searchName);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Chocolate Cake", result.get(0).getName());
        assertEquals(1, result.get(0).getIngredients().size());
        assertEquals("Sugar", result.get(0).getIngredients().get(0).getName());

        // Verify method calls
        verify(entityManager, times(1)).createQuery(anyString(), eq(Recipe.class));
        verify(typedQuery, times(1)).setParameter("namePart", searchName);
        verify(typedQuery, times(1)).getResultList();
    }

    @Test
    @DisplayName("Рецепт поиска по имени должен вернуть пустой список, когда не найдено рецептов")
    void searchRecipeByName_ShouldReturnEmptyList_WhenNoRecipesFound() {
        // Arrange
        String searchName = "Pizza";

        when(entityManager.createQuery(anyString(), eq(Recipe.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("namePart", searchName)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of());

        // Act
        List<Recipe> result = recipeService.searchRecipeByName(searchName);

        // Assert
        assertEquals(0, result.size());

        // Verify method calls
        verify(entityManager, times(1)).createQuery(anyString(), eq(Recipe.class));
        verify(typedQuery, times(1)).setParameter("namePart", searchName);
        verify(typedQuery, times(1)).getResultList();
    }

    @Test
    @DisplayName("Поиск по имени ингредиента должен вернуть список, когда найдены рецепты")
    void searchByIngredientName_ShouldReturnList_WhenRecipesFound() {
        // Arrange
        String ingredientName = "Сахар";
        Recipe recipe = new Recipe();
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Сахар");
        recipe.setIngredients(List.of(ingredient));

        // Mock напрямую TypedQuery
        TypedQuery<Recipe> typedQueryMock = mock(TypedQuery.class);
        // Установим поведение для EntityManager, чтобы вернуть TypedQuery mock
        when(entityManager.createQuery(anyString(), eq(Recipe.class))).thenReturn(typedQueryMock);
        // Mock setParameter, чтобы вернуть тот же запрос
        when(typedQueryMock.setParameter(anyString(), any())).thenReturn(typedQueryMock);

        // Настройка getResultList для возврата ожидаемого списка
        when(typedQueryMock.getResultList()).thenReturn(List.of(recipe));

        // Act
        List<Recipe> result = recipeService.searchByIngredientName(ingredientName);

        // Assert
        assertEquals(1, result.size());
        assertEquals(ingredientName, result.get(0).getIngredients().get(0).getName());
    }
}
