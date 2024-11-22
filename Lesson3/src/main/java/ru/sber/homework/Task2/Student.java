package ru.sber.homework.Task2;

public record Student(String name, int grade, String faculty) implements Comparable<Student> {

    @Override
    public int compareTo(Student other) {
        return Integer.compare(this.grade, other.grade);
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", grade=" + grade +
                ", faculty='" + faculty + '\'' +
                '}';
    }
}

