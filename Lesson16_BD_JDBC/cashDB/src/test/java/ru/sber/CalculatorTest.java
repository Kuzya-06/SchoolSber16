package ru.sber;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class CalculatorTest {
    private final Calculator calculator = new Calculator();

    @Test
    void fibonachi_send5_returnList() {
        List<Integer> fibonachi = calculator.fibonachi(5);
        Assertions.assertEquals(5, fibonachi.get(fibonachi.size() - 1));
        Assertions.assertEquals(6, fibonachi.size());
    }

    @Test
    void fibonachi_send0_returnList() {
        List<Integer> fibonachi = calculator.fibonachi(0);
        Assertions.assertEquals(0, fibonachi.get(fibonachi.size() - 1));
        Assertions.assertEquals(1, fibonachi.size());
    }

    @Test
    void fibonachi_sendNegative_returnList() {
        List<Integer> fibonachi = calculator.fibonachi(-2);
        Assertions.assertEquals(0, fibonachi.size());
    }
}
