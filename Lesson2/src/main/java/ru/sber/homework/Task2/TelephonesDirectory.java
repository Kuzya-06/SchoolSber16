package ru.sber.homework.Task2;

import java.util.*;

public class TelephonesDirectory {
    static final Map<String, Set<String>> telephoneDirectory = new HashMap<>();

    static void add(String surname, String telephone) {
        telephoneDirectory.computeIfAbsent(surname, k -> new HashSet<>()).add(telephone);
    }

    static Set<String> get(String surname) {
        return telephoneDirectory.getOrDefault(surname, Collections.emptySet());
    }

    static Map<String, Set<String>> getAll(){
        return Collections.unmodifiableMap(telephoneDirectory);
    }
}
