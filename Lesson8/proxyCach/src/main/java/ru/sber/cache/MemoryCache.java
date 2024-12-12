package ru.sber.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryCache {
    private static final MemoryCache INSTANCE = new MemoryCache();
    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    private MemoryCache() {}

    public static MemoryCache getInstance() {
        return INSTANCE;
    }

    public Object get(String key) {
        return cache.get(key);
    }

    public void put(String key, Object value) {
        cache.put(key, value);
    }
}
