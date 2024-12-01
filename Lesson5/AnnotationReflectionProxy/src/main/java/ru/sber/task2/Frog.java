package ru.sber.task2;

public class Frog extends Animal implements Amphibious {

    @Override
    public void eat() {
        System.out.println("Лягушка ест насекомых.");
    }

    @Override
    public void move() {
        System.out.println("Лягушка прыгает.");
    }

    @Override
    public void swim() {
        System.out.println("Лягушка плавает.");
    }

    protected void croak() {
        System.out.println(info());
    }

    private String info(){
        return "Лягушка квакает: Ква-ква!";
    }
}

