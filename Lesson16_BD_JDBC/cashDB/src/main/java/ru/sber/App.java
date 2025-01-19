package ru.sber;

import ru.sber.cash.CacheManager;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {

        Calculator calculator = new Calculator();
        Method method = calculator.getClass().getMethod("fibonachi", int.class);

        Scanner scanner = new Scanner(System.in);
        int n=0;
        while (n>=0){
        System.out.println("Введите число - Положение в последовательности Фибоначчи");
        n = scanner.nextInt();
        List<Integer> resultScanner = CacheManager.getCachedResult(calculator, method, n);
        System.out.println(resultScanner);}
    }
}
