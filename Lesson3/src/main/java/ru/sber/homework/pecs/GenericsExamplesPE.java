package ru.sber.homework.pecs;

import java.util.ArrayList;
import java.util.List;


class Pear extends Fruit { //груша
    @Override
    public String toString() {
        return "I am an Pear !!";
    }
}


// Используйте подстановочный знак <? extends T>, если вам нужно извлечь объект типа T из коллекции.
public class GenericsExamplesPE
{
    public static void main(String[] args)
    {
        //Список яблок
        List<Apple> apples = new ArrayList<Apple>();
        apples.add(new Apple());
        apples.add(new Apple());
        apples.add(new AsianApple());
//        apples.add(new Fruit()); //Compile time error
//        apples.add(new Pear()); //Compile time error

        //Мы можем назначить список яблок корзине с фруктами; потому что яблоко - это подвид фрукта
        List<? extends Fruit> basket = apples;

        //Here we know that in basket there is nothing but fruit only -
        // Здесь мы знаем, что в корзине нет ничего, кроме фруктов.
        for (Fruit fruit : basket)
        {
            System.out.println(fruit);
        }

        System.out.println(basket.get(0));

//        basket.add(new Apple()); //Compile time error
//        basket.add(new Fruit()); //Compile time error
//        basket.add(new AsianApple()) //Compile time error
    }
}
