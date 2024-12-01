package ru.sber.task1;

public class Main {
    public static void main(String[] args) {
        int number = 5;
        int number2 = -5;
        Calculator calculator = new CalculatorImpl();

        int factorial = calculator.calc(number);
        System.out.println(factorial);


        int factorial2 = calculator.calc(number2);
        System.out.println(factorial2);
    }
}