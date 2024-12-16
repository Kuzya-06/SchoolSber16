package ru.sber.Task1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FactorialCalculatorThreadApp {
    public static void main(String[] args) {
        String filePath = PathUtils.FILEPATH;

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            List<Integer> numbers = lines.stream()
                    .map(Integer::parseInt)
                    .toList();

            // Запускаем отдельный поток для каждого числа
            for (Integer number : numbers) {
                Thread thread = new Thread(new FactorialTask(number));
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }catch (NumberFormatException e) {
            System.err.println("Файл должен содержать только натуральные числа!");
        }
    }

    private static long calculateFactorialRecursive(int n) {
        if (n <= 1) {
            return 1;
        }
        return n * calculateFactorialRecursive(n - 1);
    }


    static class FactorialTask implements Runnable {
        private final int number;

        public FactorialTask(int number) {
            this.number = number;
        }

        @Override
        public void run() {
            try {
                long factorial = calculateFactorialRecursive(number);
                System.out.printf("Факториал числа %d = %d%n", number, factorial);
            } catch (StackOverflowError e) {
                System.err.printf("Ошибка: рекурсия слишком глубока для числа %d%n", number);
            }
        }
    }
}
