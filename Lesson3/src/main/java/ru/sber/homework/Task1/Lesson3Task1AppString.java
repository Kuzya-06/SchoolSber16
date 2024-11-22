package ru.sber.homework.Task1;

import java.util.HashMap;
import java.util.Map;

public class Lesson3Task1AppString {

    private final static int COUNT_REPEAT = 20;

    public static void main(String[] args) {

        CountMap<String> map = new CountMapImpl<>();

        map.add("Bob");
        map.add("Bob");
        map.add("Anna");
        map.add("Thomas");
        map.add("Anna");
        map.add("Bob");

        System.out.println("\nПосле добавления элементов в CountMap => "+map.toMap());
        System.out.println("Количество добавлений элемента 5 => " + map.getCount("Anna")); //2
        System.out.println("Количество добавлений элемента 6 => " + map.getCount("Thomas")); //1
        System.out.println("Количество добавлений элемента 10 => " + map.getCount("Bob")); //3
        System.out.println("Количество разных элементов = " + map.size()); //3
        System.out.println("Удалили элемент 5 у которого количество добавлений => " + map.remove("Anna")); //2
        System.out.println("Количество разных элементов стало = " + map.size()); //2
        System.out.println("После удаления элемента 5 в CountMap => "+map.toMap());

        System.out.println("-".repeat(COUNT_REPEAT));

        CountMap<String> mapNew = new CountMapImpl<>();
        mapNew.add("Leo");
        mapNew.add("Bob");
        mapNew.add("Orlando");
        mapNew.add("Thomas");
        System.out.println("После добавления элементов в новый CountMap => "+mapNew.toMap());
        map.addAll(mapNew);
        System.out.println("После добавления элементов из нового CountMap в старый =>"+map.toMap());
        System.out.println("-".repeat(COUNT_REPEAT));

        Map<String, Integer> mapDestination = new HashMap<>();
        mapDestination.put("Leo", 1);
        mapDestination.put("Julian", 7);
        mapDestination.put("Christopher", 9);
        mapDestination.put("Bob", 1);
        System.out.println("Вся информация сейчас в CountMap => "+map.toMap());
        System.out.println("Создан новый mapDestination => "+mapDestination);
        map.toMap(mapDestination, true);
        System.out.println("Вся информация записана в новый mapDestination => "+mapDestination);
        System.out.println("-".repeat(COUNT_REPEAT));
    }

}
