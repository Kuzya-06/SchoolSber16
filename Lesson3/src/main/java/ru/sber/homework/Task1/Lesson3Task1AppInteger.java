package ru.sber.homework.Task1;

import java.util.HashMap;
import java.util.Map;

public class Lesson3Task1AppInteger {

    private final static int COUNT_REPEAT = 20;

    public static void main(String[] args) {

        CountMap<Integer> map = new CountMapImpl<>();
        map.add(10);
        map.add(10);
        map.add(5);
        map.add(6);
        map.add(5);
        map.add(10);

        System.out.println("\nПосле добавления элементов в CountMap => "+map.toMap());
        System.out.println("Количество добавлений элемента 5 => " + map.getCount(5)); //2
        System.out.println("Количество добавлений элемента 6 => " + map.getCount(6)); //1
        System.out.println("Количество добавлений элемента 10 => " + map.getCount(10)); //3
        System.out.println("Количество разных элементов = " + map.size()); //3
        System.out.println("Удалили элемент 5 у которого количество добавлений => " + map.remove(5)); //2
        System.out.println("Количество разных элементов стало = " + map.size()); //2
        System.out.println("После удаления элемента 5 в CountMap => "+map.toMap());

        System.out.println("-".repeat(COUNT_REPEAT));

        CountMap<Integer> mapNew = new CountMapImpl<>();
        mapNew.add(110);
        mapNew.add(110);
        mapNew.add(13);
        mapNew.add(10);
        System.out.println("После добавления элементов в новый CountMap => "+mapNew.toMap());
        map.addAll(mapNew);
        System.out.println("После добавления элементов из нового CountMap в старый =>"+map.toMap());
        System.out.println("-".repeat(COUNT_REPEAT));

        Map<Integer, Integer> mapDestination = new HashMap<>();
        mapDestination.put(6, 3);
        mapDestination.put(-8, 1);
        mapDestination.put(-15, 1);
        mapDestination.put(110, 3);
        System.out.println("Вся информация сейчас в CountMap => "+map.toMap());
        System.out.println("Создан новый mapDestination => "+mapDestination);
        map.toMap(mapDestination);
        System.out.println("Вся информация записана в новый mapDestination => "+mapDestination);
        System.out.println("-".repeat(COUNT_REPEAT));

    }

}
