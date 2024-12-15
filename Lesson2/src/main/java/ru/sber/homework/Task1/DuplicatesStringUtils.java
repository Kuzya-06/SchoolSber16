package ru.sber.homework.Task1;

import java.util.HashMap;
import java.util.Map;

public class DuplicatesStringUtils {

    static Map<String, Integer> findDuplicatesString(String[] strArr) {
        Map<String, Integer> map = new HashMap<>();
        for (String str : strArr) {
            map.put(str, map.getOrDefault(str, 0) + 1);
        }
        return map;
    }


    static void printInfoArray(Map<String, Integer> map) {
        System.out.println("Уникальные слова: ");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                System.out.print(" " + entry.getKey());
            }
        }

        System.out.println("\nЧастота встреч каждого слова:");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println("Слово '" + entry.getKey() + "' встречается " + entry.getValue() + " раз(а).");
        }
    }

}
