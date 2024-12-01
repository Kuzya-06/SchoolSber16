package ru.sber.task2;

public class Main {
    public static void main(String[] args) {

        Class<?> aClass = RedFrog.class;

        ReflectionUtils.printAllMethods(aClass);
    }
}
