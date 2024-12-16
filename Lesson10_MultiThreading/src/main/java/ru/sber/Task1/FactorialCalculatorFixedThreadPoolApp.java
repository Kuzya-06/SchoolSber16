package ru.sber.Task1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FactorialCalculatorFixedThreadPoolApp {
    public static void main(String[] args) {
        String filePath = PathUtils.FILEPATH;

        try {

            // Читаем числа из файла
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            List<Integer> numbers = lines.stream()
                    .map(Integer::parseInt)
                    .toList();

            // Создаем пул потоков
            ExecutorService executorService = Executors.newFixedThreadPool(2); // x - количество потоков

            // Отправляем задачи на выполнение
            for (Integer number : numbers) {

                executorService.submit(() -> {
                    long factorial = calculateFactorial(number);
                    System.out.printf("Факториал числа %d = %d%n", number, factorial);
                });
            }

            // Останавливаем пул потоков
            executorService.shutdown();

        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Файл должен содержать только натуральные числа!");
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
}
