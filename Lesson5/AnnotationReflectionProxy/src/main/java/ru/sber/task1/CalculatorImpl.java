package ru.sber.task1;

public class CalculatorImpl implements Calculator {
    @Override
    public int calc(int number) {
        if (number <= 1) {
            return 1;
        } else {
            return number * calc(number - 1);
        }
    }

    private void countCrow() {
        System.out.println("Вызван countCrow");
    }

    private static String countCrow(String arg) {
        String s = "Вызван countCrow с аргументом " + arg;
        System.out.println(s);
        return s;
    }

}
