package ru.sber.cache;

public class CacheEntry<T> {
    private final T value;
    private final long creationTime;

    public CacheEntry(T value) {
        this.value = value;
        this.creationTime = System.currentTimeMillis();
    }

    public T getValue() {
        return value;
    }

    public long getCreationTime() {
        return creationTime;
    }
}

