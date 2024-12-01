package ru.sber.task6;

import ru.sber.task1.Calculator;
import ru.sber.task1.CalculatorImpl;

public class Main {
    public static void main(String[] args) {

        Calculator calculator = PerformanceProxy.createProxy(new CalculatorImpl(), Calculator.class);

        // Тестируем метод
        System.out.println("Результат: " + calculator.calc(3));
        System.out.println("Результат: " + calculator.calc(3));
        System.out.println("Результат: " + calculator.calc(6));
        System.out.println("Результат: " + calculator.calc(6));
        System.out.println("Результат: " + calculator.calc(12));
    }
}
