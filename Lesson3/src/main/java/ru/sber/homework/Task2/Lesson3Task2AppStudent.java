package ru.sber.homework.Task2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Lesson3Task2AppStudent {
    public static void main(String[] args) {
        List<Student> students = Arrays.asList(
                new Student("Alice", 85, "Mathematics"),
                new Student("Bob", 90, "History"),
                new Student("Charlie", 70, "Economy"),
                new Student("Diana", 95, "Mathematics"),
                new Student("Eve", 80, "Mathematics"),
                new Student("Tom", 70, "Literature"),
                new Student("Tom", 88, "Technology")
        );

        // Используем Comparable (grade)
        List<Student> result1 = CollectionUtils.range(students,
                new Student("", 80, ""),
                new Student("", 90, ""));

        System.out.println("\nRange (80 to 90, Comparable): ");
        result1.forEach(System.out::println);

        // Используем Comparator (grade)
        List<Student> result2 = CollectionUtils.range(students,
                new Student("", 80, "Mathematics"),
                new Student("", 90, "History"),
                Comparator.comparingInt(Student::grade));

        System.out.println("\nRange (80 to 90, Comparator): ");
        result2.forEach(System.out::println);

        // Используем Comparator (faculty)
        List<Student> result3 = CollectionUtils.range(students,
                new Student("", 80, "History"),
                new Student("", 90, "Mathematics"),
                Comparator.comparing(Student::faculty));

        System.out.println("\nRange (History to Mathematics, Comparator): ");
        result3.forEach(System.out::println);

    }
}
