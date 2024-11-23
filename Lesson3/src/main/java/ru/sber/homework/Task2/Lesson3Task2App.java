package ru.sber.homework.Task2;

import java.util.List;

public class Lesson3Task2App {

    private final static int COUNT_REPEAT = 20;

    public static void main(String[] args) {

        List<String> list1 = CollectionUtils.newArrayList();
        list1.add("Bob"); // 0
        list1.add("Bob"); // 1
        list1.add("Adam"); // 2
        list1.add("Thomas"); // 3
        list1.add("Georgy");
        list1.add("David");
        System.out.println("Коллекция 1 => " + list1);

        List<String> list2 = CollectionUtils.newArrayList();
        list2.add("Samanta");
        list2.add("Anna");
        list2.add("Anna");
        list2.add("Megan");
        list2.add("Jane");
        list2.add("Hannah");
        System.out.println("Коллекция 2 => " + list2);

        CollectionUtils.addAll(list1, list2);
        System.out.println("Коллекция 2 после добавления коллекции 1 => " + list2);
        System.out.println("-".repeat(COUNT_REPEAT));

        System.out.println("Позиция элемента \"Thomas\" в коллекции 1 = " + CollectionUtils.indexOf(list1, "Thomas"));
        // 3
        System.out.println("Позиция элемента \"Thomas123\" в коллекции 1 = " + CollectionUtils.indexOf(list1,
                "Thomas123")); // -1
        System.out.println("-".repeat(COUNT_REPEAT));

        System.out.println("Список, содержащий первые size(10) элементов => " + CollectionUtils.limit(list2, 10));
        System.out.println("-".repeat(COUNT_REPEAT));

        CollectionUtils.removeAll(list2, list1);
        System.out.println("Коллекция 2 после removeAll() => " + list2);
        System.out.println("-".repeat(COUNT_REPEAT));

        List<String> list3 = CollectionUtils.newArrayList();
        list3.add("Samanta");
        list3.add("Anna");
        list3.add("Anna");
        list3.add("Megan");
        list3.add("Jane");
        list3.add("Hannah");
        System.out.println("Коллекция 3 => " + list3);
        System.out.println("Коллекция 2 содержит все элементы коллекции 3 = " + CollectionUtils.containsAll(list2,
                list3));
        System.out.println("-".repeat(COUNT_REPEAT));

        List<String> list4 = CollectionUtils.newArrayList();
        list4.add("Ryan");
        list4.add("Lucas");
        list4.add("Hannah");
        System.out.println("Коллекция 4 => " + list4);
        System.out.println("Коллекция 2 содержит хотя-бы 1 элемент коллекции 4 = " + CollectionUtils.containsAny(list2,
                list4));
        System.out.println("-".repeat(COUNT_REPEAT));
    }

}
