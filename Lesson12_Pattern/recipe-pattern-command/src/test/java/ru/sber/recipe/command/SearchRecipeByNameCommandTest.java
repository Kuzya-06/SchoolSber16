package ru.sber.recipe.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.sber.recipe.service.RecipeManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SearchRecipeByNameCommandTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Mock
    private RecipeManager mockRecipeManager;

    @Mock
    private Scanner mockScanner;

    private SearchRecipeByNameCommand searchRecipeByNameCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        searchRecipeByNameCommand = new SearchRecipeByNameCommand(mockRecipeManager, mockScanner);

        // Перехват вывода в консоль
        System.setOut(new PrintStream(outputStream));
    }

    @BeforeEach
    void tearDown() {
        // Восстановление стандартного вывода в консоль
        System.setOut(originalOut);
    }

    @Test
    void testExecute_RecipesFound() {
        String searchName = "суп";
        List<String> foundRecipes = Arrays.asList("Суп с курицей", "Суп с грибами, Борщ красный");

        when(mockScanner.nextLine()).thenReturn(searchName);
        when(mockRecipeManager.searchRecipeByName(searchName)).thenReturn(foundRecipes);

        searchRecipeByNameCommand.execute();

        String consoleOutput = outputStream.toString();
        assertThat(consoleOutput).contains("Найденные рецепты:");
        assertThat(consoleOutput).contains("Суп с курицей");
        assertThat(consoleOutput).contains("Суп с грибами");

        verify(mockScanner, times(1)).nextLine();
        verify(mockRecipeManager, times(1)).searchRecipeByName(searchName);
    }

    @Test
    void testExecute_NoRecipesFound() {
        String searchName = "Test";
        List<String> foundRecipes = Collections.emptyList();

        when(mockScanner.nextLine()).thenReturn(searchName);
        when(mockRecipeManager.searchRecipeByName(searchName)).thenReturn(foundRecipes);

        searchRecipeByNameCommand.execute();

        String consoleOutput = outputStream.toString();
        assertThat(consoleOutput).contains("Рецепты не найдены.");

        verify(mockScanner, times(1)).nextLine();
        verify(mockRecipeManager, times(1)).searchRecipeByName(searchName);
    }

}