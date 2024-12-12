package ru.sber.model;

public record Person(String name, int age) {

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + '}';
    }
}
