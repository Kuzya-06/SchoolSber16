package ru.sber.Task1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FactorialCalculatorCallableApp {
    public static void main(String[] args) {
        String filePath = PathUtils.FILEPATH;

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            List<Integer> numbers = lines.stream()
                    .map(Integer::parseInt)
                    .toList();

            // Создаем пул потоков
            ExecutorService executorService = Executors.newFixedThreadPool(1); // x - количество потоков

            List<Callable<String>> tasks = new ArrayList<>();
            for (Integer number : numbers) {
                tasks.add(() -> {
                    long factorial = calculateFactorialRecursive(number);
                    return String.format("Факториал числа %d = %d", number, factorial);
                });
            }

            //получаем результаты
            List<Future<String>> results = executorService.invokeAll(tasks);

            for (Future<String> result : results) {
                System.out.println(result.get());
            }
            executorService.shutdown();
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Файл должен содержать только натуральные числа!");
        } catch (Exception e) {
            System.err.println("Ошибка при выполнении задач: " + e.getMessage());
        }
    }

    // Метод для вычисления факториала
    private static long calculateFactorial(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    // Метод для вычисления факториала с помощью рекурсии
    private static long calculateFactorialRecursive(int n) {
        if (n <= 1) {
            return 1;
        }
        return n * calculateFactorialRecursive(n - 1);
    }
}
