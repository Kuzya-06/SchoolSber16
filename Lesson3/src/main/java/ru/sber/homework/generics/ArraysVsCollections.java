package ru.sber.homework.generics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArraysVsCollections {
    public static void main(String[] args) {
        run(new Integer[10]); // ковариантность
        run(new Double[10]);


        List<Number> numbers = new ArrayList<Number>();
//        List<Number> numbers2 = new ArrayList<Integer>(); // compile error

    }

    private static void run(Number[] numbers) {
        numbers[0] = 1; // Только для Integer
        numbers[1] = 1d; // Только для Double
        numbers[2] = 10L; // Только для Long
    }
}
