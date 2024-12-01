package ru.sber.task5;

import ru.sber.task1.Calculator;
import ru.sber.task1.CalculatorImpl;

public class Main {
    public static void main(String[] args) {
        // Оригинальная реализация
        Calculator calculator = new CalculatorImpl();

        // Кэширующий прокси
        Calculator cachingCalculator = CachingProxy.createProxy(calculator, Calculator.class);

        // Тестовые вызовы
        System.out.println("Результат 1: " + cachingCalculator.calc(14)+"\n");
        System.out.println("Результат 2: " + cachingCalculator.calc(15)+"\n");
        System.out.println("Результат 3: " + cachingCalculator.calc(14)+"\n"); // из кэша
        System.out.println("Результат 4: " + cachingCalculator.calc(16)+"\n");
    }
}

