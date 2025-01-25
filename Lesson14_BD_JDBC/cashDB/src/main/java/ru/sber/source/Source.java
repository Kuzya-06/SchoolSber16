package ru.sber.source;

import java.util.List;

/**
 * Определяет, как мы будем сохранять и извлекать данные из кэша
 */
public interface Source {
    void save(String key, List<Integer> data);
    List<Integer> load(String key);
}
