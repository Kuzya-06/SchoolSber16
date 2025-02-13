package ru.sber.recipe.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.sber.recipe.exceptions.InvalidInputException;

import java.util.Scanner;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CommandExecutorTest {

    @Mock
    private Command mockCommand;

    @Mock
    private Scanner mockScanner;

    private CommandExecutor commandExecutor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commandExecutor = new CommandExecutor(mockScanner);
    }

    @Test
    @DisplayName("Зарегистрированная команда")
    void testExecuteCommand_RegisteredCommand() {
        commandExecutor.registerCommand(1, mockCommand);

        commandExecutor.executeCommand(1);

        verify(mockCommand, times(1)).execute();
    }

    @Test
    @DisplayName("Не зарегистрированная команда")
    void testExecuteCommand_UnregisteredCommand() {
        commandExecutor.executeCommand(99);

        verify(mockCommand, never()).execute();
    }

    @Test
    @DisplayName("Команда, которая выбрасывает исключение")
    void testExecuteCommand_CommandThrowsException() {
        doThrow(new InvalidInputException("Ошибка при выполнении команды: ")).when(mockCommand).execute();
        // Регистрируем команду, которая выбрасывает исключение
        commandExecutor.registerCommand(1, mockCommand);

        assertDoesNotThrow(() -> commandExecutor.executeCommand(1));

        // Проверка, что команда была выполнена
        verify(mockCommand, times(1)).execute();
    }

}
