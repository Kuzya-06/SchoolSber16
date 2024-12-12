package ru.sber.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class MyStream<T> {
    private final List<T> collection;

    private MyStream(List<T> collection) {
        this.collection = new ArrayList<>(collection);
    }

    public static <T> MyStream<T> of(List<? extends T> list) {
        return (MyStream<T>) new MyStream<>(list);
    }

    /**
     * Метод filter для фильтрации элементов
     *
     * @param predicate
     * @return
     */
    public MyStream<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate не должен быть null");
        Predicate<T> filterPredicate = new Predicate<T>() {
            @Override
            public boolean test(T item) {
                return predicate.test(item);
            }
        };

        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (!filterPredicate.test(item)) {
                iterator.remove();
            }
        }
        return this;
    }


    /**
     * Метод transform для преобразования элементов
     *
     * @param transformer
     * @param <R>
     * @return
     */
    public <R> MyStream<R> transform(Function<? super T, ? extends R> transformer) {
        Objects.requireNonNull(transformer, "transformer не должен быть null");
        List<R> transformed = new ArrayList<>();
        for (T item : collection) {
            R transformedItem = transformer.apply(item); // Преобразую элемент
            transformed.add(transformedItem);           // Добавляю преобразованный элемент в результат
        }
        return new MyStream<>(transformed);
    }

    /**
     * Метод toMap для преобразования в Map
     * @param keyMapper
     * @param valueMapper
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper,
                                  Function<? super T, ? extends V> valueMapper) {
        Objects.requireNonNull(keyMapper, "Ключ map не должен быть null");
        Objects.requireNonNull(valueMapper, "Value map не должен быть null");

        Map<K, V> resultMap = new HashMap<>();
        for (T item : collection) {
            K key = keyMapper.apply(item);
            V value = valueMapper.apply(item);
            try {
                if (resultMap.put(key, value) != null) {
                    throw new IllegalStateException("Дубликат key: " + key);
                }
            } catch (IllegalStateException e) {
                System.err.println("e.getMessage() = " + e.getMessage());
            }

        }
        return resultMap;
    }
}
