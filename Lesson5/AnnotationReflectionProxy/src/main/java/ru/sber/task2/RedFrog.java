package ru.sber.task2;

public class RedFrog extends Frog implements Fly {

    private String color;

    public String getColor() {
        return color;
    }

    @Override
    public void eat() {
        System.out.println("Красная Лягушка ест насекомых.");
    }

    @Override
    public void move() {
        System.out.println("Красная Лягушка прыгает.");
    }

    @Override
    public void swim() {
        System.out.println("Красная Лягушка плавает.");
    }

    @Override
    public void fly() {
        System.out.println("Красная Лягушка летает.");
    }
}

