package ru.sber.homework.pecs;

import java.util.ArrayList;
import java.util.List;


class RedAsianApple extends AsianApple {
    @Override
    public String toString() {
        return "I am an RedAsianApple !!\n";
    }
}

// Используйте подстановочный знак <? super T>, если вам нужно поместить объекты типа T в коллекцию.
public class GenericsExamplesCS
{
    public static void main(String[] args)
    {
        //List of apples
        List<Apple> apples = new ArrayList<Apple>();
        apples.add(new Apple());
        apples.add(new AsianApple());
        apples.add(new RedAsianApple());

        //We can assign a list of apples to a basket of apples - Мы можем назначить список яблок корзине с яблоками.
        List<? super Apple> basket = apples;

        basket.add(new Apple());    //Successful
        basket.add(new AsianApple()); //Successful
//        basket.add(new Fruit());    //Compile time error

        System.out.println(basket);
        System.out.println(basket.get(0));
        System.out.println(basket.get(1));
        System.out.println(basket.get(2));
    }
}
