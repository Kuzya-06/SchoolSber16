package ru.sber.task3;

import ru.sber.task2.RedFrog;

public class Main {
    public static void main(String[] args) {

        Class<?> aClass = RedFrog.class;

        GetterFinderUtils.printAllGetters(aClass);
    }
}
