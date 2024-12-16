package ru.sber.Task1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FactorialCalculatorApp {
    public static void main(String[] args) throws IOException {
        String filePath = PathUtils.FILEPATH;

        // Чтение числа из файла
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        List<Integer> numbers = lines.stream()
                .map(Integer::parseInt)
                .toList();


        // Выполнение
        for (Integer number : numbers) {
            long l = calculateFactorialRecursive(number);
            System.out.printf("Факториал числа %d = %d\n", number, l);
        }
    }

    private static long calculateFactorialRecursive(int n) {
        if (n <= 1) {
            return 1;
        }
        return n * calculateFactorialRecursive(n - 1);
    }
}
