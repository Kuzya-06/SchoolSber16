package ru.sber;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ObjectArrayIterator<T> implements Iterator<T> {
    private final T[] array;
    private int index = 0;

    public ObjectArrayIterator(T[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Массив не может быть null");
        }
        this.array = array;
    }

    @Override
    public boolean hasNext() {
        return index < array.length;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("В массиве нет элементов");
        }
        return array[index++];
    }
}
