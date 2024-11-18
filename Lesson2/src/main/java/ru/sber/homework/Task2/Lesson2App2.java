package ru.sber.homework.Task2;

public class Lesson2App2 {
    public static void main(String[] args) {

        TelephonesDirectory.add("Danilov", "123");
        TelephonesDirectory.add("Danilov", "123");
        TelephonesDirectory.add("Antonov", "456");
        TelephonesDirectory.add("Ivanov", "2-456-99-00");
        TelephonesDirectory.add("Antonov", "987");

        System.out.println(TelephonesDirectory.get("Danilov"));
        System.out.println(TelephonesDirectory.get("Antonov"));
        System.out.println(TelephonesDirectory.get("Antonov2"));
        System.out.println(TelephonesDirectory.get("Ivanov"));

        System.out.println(TelephonesDirectory.getAll());
    }
}
