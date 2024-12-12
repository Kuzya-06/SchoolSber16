package ru.sber;

import ru.sber.api.MyStream;
import ru.sber.model.Person;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Person> people = List.of(
                new Person("Инга", 25),
                new Person("Ира", 19),
                new Person("Игорь", 36),
                new Person("Инна", 30),
                new Person("Инна", 30),
                new Person("Илья", 32)
        );

        Map<String, Person> result = MyStream.of(people)
                .filter(p -> p.age() > 20 && p.name().startsWith("И"))
                .filter(p -> p.age() < 35)
                .transform(p -> new Person(p.name()+"!", p.age() + 30))
                .toMap(Person::name, p -> p);

        System.out.println("Оригинальная коллекция: " + people);
        System.out.println("Результат:              " + result+"\n");

        Map<Integer, Person> result2 = MyStream.of(people)
                .filter(p -> p.age() > 20 && p.name().startsWith("И"))
                .filter(p -> p.age() < 35)
                .transform(p -> new Person(p.name()+"!", p.age() + 30))
                .toMap(Person::age, p -> p);

        System.out.println("Оригинальная коллекция: " + people);
        System.out.println("Результат:              " + result2);
    }
}
