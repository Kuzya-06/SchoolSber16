package ru.sber.recipe.command;

import ru.sber.recipe.exceptions.InvalidInputException;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandExecutor {
    private final Map<Integer, Command> commands = new HashMap<>();
    private final Scanner scanner;

    public CommandExecutor(Scanner scanner) {
        this.scanner = scanner;
    }

    public void registerCommand(int choice, Command command) {
        commands.put(choice, command);
    }

    public void executeCommand(int choice) {
        Command command = commands.get(choice);
        if (command != null) {
            try {
                command.execute();
            } catch (InvalidInputException e) {
                System.err.println(e.getMessage());
            }
        } else {
            System.out.println("Неверный выбор. Попробуйте снова.");
        }
    }

}
