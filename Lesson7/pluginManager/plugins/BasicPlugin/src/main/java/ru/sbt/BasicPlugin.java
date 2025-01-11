package ru.sbt;

public class BasicPlugin implements Plugin {
    @Override
    public void doUsefull() {
        System.out.println("Привет, я базовый плагин");
    }
}
