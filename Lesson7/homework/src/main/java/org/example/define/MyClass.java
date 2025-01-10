package org.example.define;

public class MyClass {
    private int count = 2;

    public void print() {
        System.out.println(++count);
    }

    public void print(int count) {
        System.out.println(++count);
    }

    public void sayHello() {
        System.out.println("Hello from MyClass!");
    }
}
