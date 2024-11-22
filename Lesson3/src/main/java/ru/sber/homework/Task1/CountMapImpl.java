package ru.sber.homework.Task1;

import java.util.HashMap;
import java.util.Map;

public class CountMapImpl<K> implements CountMap<K> {

    private final Map<K, Integer> map = new HashMap<>();

    @Override
    public void add(K o) {
        map.put(o, map.getOrDefault(o, 0) + 1);
    }

    @Override
    public int getCount(K o) {
        return map.getOrDefault(o, 0);
    }

    @Override
    public int remove(K o) {
        int temp = this.getCount(o);
        map.remove(o);
        return temp;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void addAll(CountMap<K> source) {
        source.toMap().forEach((key, value) -> map.put(key, map.getOrDefault(key, 0) + value));
    }

    @Override
    public Map<K, Integer> toMap() {
        return new HashMap<>(map);
    }

    @Override
    public void toMap(Map<K, Integer> destination) {
        // По умолчанию работает как combine = false (перезаписывает значения)
        toMap(destination, false);
    }

    @Override
    public void toMap(Map<K, Integer> destination, boolean combine) {
        if (combine) {
            // Суммировать значения для совпадающих ключей
            for (Map.Entry<K, Integer> entry : map.entrySet()) {
                destination.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }
        } else {
            // Просто перезаписывать значения
            destination.clear();
            destination.putAll(map);
        }
    }

}
