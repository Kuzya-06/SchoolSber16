package ru.sber.homework.Task2;

import java.util.*;

public class TelephonesDirectory {
    static final Map<String, Set<String>> telephoneDirectory = new HashMap<>();

    static {
        Set<String> telephones = new HashSet<>();
        telephones.add("8-903-123-45-67");
        telephones.add("8-906-789-01-23");
        telephoneDirectory.put("Danilov", telephones);
    }

    static void add(String surname, String telephone) {
        if (telephoneDirectory.containsKey(surname)) {
            telephoneDirectory.get(surname).add(telephone);
        } else {
            Set<String> telephones = new HashSet<>();
            telephones.add(telephone);
            telephoneDirectory.put(surname, telephones);
        }
    }

    static Set<String> get(String surname) {
        return telephoneDirectory.getOrDefault(surname, Collections.emptySet());
    }

    static Map<String, Set<String>> getAll(){
        return telephoneDirectory;
    }
}
