package ru.sber.homework.generics;

import java.util.ArrayList;
import java.util.List;

/**
 * Стирание типов
 */
public class TypeErasure {
    public static void main(String[] args) {
        List<Number> numbers = new ArrayList<Number>();
        List<Integer> integers = new ArrayList<Integer>();

        List temp = integers;
        numbers = temp;
//        numbers = integers; // No compile List<Numbers> != List<Integer>

        numbers.add(2);
        numbers.add(2d);
        Integer integer = integers.get(0);
//        Integer integer1 = integers.get(1); // java.lang.ClassCastException: class java.lang.Double cannot be cast to class java.lang.Integer


    }
}
