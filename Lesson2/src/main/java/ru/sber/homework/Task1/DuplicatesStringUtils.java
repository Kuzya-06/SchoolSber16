package ru.sber.homework.Task1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuplicatesStringUtils {

    static Map<String, Integer> findDuplicatesString(String[] strArr) {
        Map<String, Integer> map = new HashMap<>();

        for (String s : strArr) {
            if (!map.containsKey(s)) {
                map.put(s, 1);
            } else {
                int count = map.get(s);
                map.put(s, ++count);
            }
        }
        return map;
    }

    static void listUniqueStrings(Map<String, Integer> map) {
        List<String> list = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                list.add(entry.getKey());
            }
        }
        System.out.println("Список уникальных слов, из которых состоит массив: "+list);
    }

    static void printInfoArray(Map<String, Integer> map){
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println("Слово "+entry.getKey() +" встречается "+entry.getValue());
            }
        }

}
