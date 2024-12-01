package ru.sber.task1;

import ru.sber.task6.Metric;

public interface Calculator {
    /**
     * Расчет факториала числа.
     *
     * <p><b>Пример:</b></p>
     * <pre>
     * calc(5)
     * вернет {120}
     * 5! = 5 * 4 * 3 * 2 * 1 = 120.
     * </pre>
     *
     * @param number заданное число
     */
    @Metric
    int calc(int number);

}
