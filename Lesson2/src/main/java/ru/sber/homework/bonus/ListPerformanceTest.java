package ru.sber.homework.bonus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListPerformanceTest {

    private static final int ELEMENT_COUNT = 10_000_000;

    public static void main(String[] args) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        LinkedList<Integer> linkedList = new LinkedList<>();

        // Заполнение списков
        initializeList(arrayList);
        initializeList(linkedList);

        System.out.printf("%-13s%-17s%s%nдля элементов =%s", " ", "ArrayList", "LinkedList", ELEMENT_COUNT);
        testListPerformanceAdd(arrayList, linkedList);
        testListPerformanceRemove(arrayList, linkedList);
        testListPerformanceGetIndex(arrayList, linkedList);
        testListPerformanceIndexOf(arrayList, linkedList);
        testListPerformanceContains(arrayList, linkedList);
        testListPerformanceSet(arrayList, linkedList);

    }

    private static void initializeList(List<Integer> list) {
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            list.add(i);
        }
    }

    private static void testListPerformanceAdd(List<Integer> arrayList, List<Integer> linkedList) {
        System.out.println("\nДобавление:");

        printInfo(measureTime(() -> arrayList.add(0, -1)), measureTime(() -> linkedList.add(0, -1)), "В начало:");

        printInfo(measureTime(() -> arrayList.add(ELEMENT_COUNT / 2, -1)),
                measureTime(() -> linkedList.add(ELEMENT_COUNT / 2, -1)), "В середину:");

        printInfo(measureTime(() -> arrayList.add(arrayList.size(), -1)),
                measureTime(() -> linkedList.add(linkedList.size(), -1)), "В конец:");
    }

    private static void testListPerformanceRemove(List<Integer> arrayList, List<Integer> linkedList) {
        System.out.println("\nУдаление:");

        printInfo(measureTime(() -> arrayList.remove(0)), measureTime(() -> linkedList.remove(0)), "В начале:");

        printInfo(measureTime(() -> arrayList.remove(ELEMENT_COUNT / 2 - 1)),
                measureTime(() -> linkedList.remove(ELEMENT_COUNT / 2 - 1)), "В середине:");

        printInfo(measureTime(() -> arrayList.remove(arrayList.size() - 1)),
                measureTime(() -> linkedList.remove(linkedList.size() - 1)), "В конце:");
    }

    private static void testListPerformanceGetIndex(List<Integer> arrayList, List<Integer> linkedList) {
        System.out.println("\nПоиск по индексу:");

        printInfo(measureTime(() -> arrayList.get(0)), measureTime(() -> linkedList.get(0)), "В начале:");

        printInfo(measureTime(() -> arrayList.get(ELEMENT_COUNT / 2)),
                measureTime(() -> linkedList.get(ELEMENT_COUNT / 2)), "В середине:");

        printInfo(measureTime(() -> arrayList.get(arrayList.size() - 1)),
                measureTime(() -> linkedList.get(linkedList.size() - 1)), "В конце:");
    }

    private static void testListPerformanceSet(List<Integer> arrayList, List<Integer> linkedList) {
        System.out.println("\nЗамена элемента:");

        printInfo(measureTime(() -> arrayList.set(0, -1)), measureTime(() -> linkedList.set(0, -1)), "В начале:");

        printInfo(measureTime(() -> arrayList.set(ELEMENT_COUNT / 2, -1)),
                measureTime(() -> linkedList.set(ELEMENT_COUNT / 2, -1)), "В середине:");

        printInfo(measureTime(() -> arrayList.set(arrayList.size() - 1, -1)),
                measureTime(() -> linkedList.set(linkedList.size() - 1, -1)), "В конце:");
    }

    private static void testListPerformanceIndexOf(List<Integer> arrayList, List<Integer> linkedList) {
        System.out.println("\nПоиск по значению:");

        printInfo(measureTime(() -> arrayList.indexOf(0)), measureTime(() -> linkedList.indexOf(0)), "В начале:");

        printInfo(measureTime(() -> arrayList.indexOf(ELEMENT_COUNT / 2)),
                measureTime(() -> linkedList.indexOf(ELEMENT_COUNT / 2)), "В середине:");

        printInfo(measureTime(() -> arrayList.indexOf(arrayList.size() - 1)),
                measureTime(() -> linkedList.indexOf(linkedList.size() - 1)), "В конце:");
    }

    private static void testListPerformanceContains(List<Integer> arrayList, List<Integer> linkedList) {
        System.out.println("\nПроверяет объект в списке:");

        printInfo(measureTime(() -> arrayList.contains(1)), measureTime(() -> linkedList.contains(1)), "В начале:");

        printInfo(measureTime(() -> arrayList.contains(ELEMENT_COUNT / 2)),
                measureTime(() -> linkedList.contains(ELEMENT_COUNT / 2)), "В середине:");

        printInfo(measureTime(() -> arrayList.contains(arrayList.size() - 1)),
                measureTime(() -> linkedList.contains(linkedList.size() - 1)), "В конце:");
    }

    private static double measureTime(Runnable task) {
        long startTime = System.nanoTime();
        task.run();
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000.0;
    }

    private static void printInfo(double arrayDouble, double linkedDouble, String operation) {
        System.out.printf("%-12s %-7.3f мс %-6s%-7.3f мс%n", operation, arrayDouble, " ", linkedDouble);
    }

}
