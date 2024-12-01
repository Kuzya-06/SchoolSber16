package ru.sber.task2;

public abstract class Animal {
    private String name;

    private int age;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public abstract void eat();

    public abstract void move();

    public void sleep() {
        System.out.println("This animal is sleeping.");
    }
}

