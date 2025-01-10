package org.example.load;

public class Person {
    public String name;
    public Integer age;

    public Person() {
    }

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public void print(){
        System.out.println("name = "+name+ " age = "+age);
    }

    public void print(String name, Integer age){
        System.out.println("name = "+name+ " age = "+age);
    }

}
