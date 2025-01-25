package ru.sber;

import ru.sber.cash.Cachable;
import ru.sber.source.DatabaseSource;

import java.util.ArrayList;
import java.util.List;

public class Calculator {

    @Cachable(DatabaseSource.class)
    public List<Integer> fibonachi(int n) {
        List<Integer> fib = new ArrayList<>();
        if (n < 0)
            return fib;
        else {
            dataProcessing();
            for (int i = 0; i <= n; i++) {
                if (i == 0) fib.add(0);
                else if (i == 1) fib.add(1);
                else {
                    fib.add(fib.get(i - 1) + fib.get(i - 2));
                }
            }
            return fib;
        }
    }

    private static void dataProcessing() {
        System.out.print("Обработка данных");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.print("\r");
        }
    }
}
