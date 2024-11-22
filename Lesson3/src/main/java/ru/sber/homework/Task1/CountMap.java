package ru.sber.homework.Task1;

import java.util.Map;

public interface CountMap<K> {
    /**
     * Добавляет элемент в этот контейнер.
     *
     * @param o Элемент для добавления
     */
    void add(K o);

    /**
     * Возвращает количество добавлений данного элемента
     *
     * @param o Элемент, у которого надо получить количество добавлений
     * @return Количество добавлений этого элемента
     */
    int getCount(K o);

    /**
     * Удаляет элемент из контейнера, и возвращает количество его добавлений(до удаления)
     *
     * @param o Элемент для удаления
     * @return Количество добавлений этого элемента перед удалением
     */
    int remove(K o);

    /**
     * Количество разных элементов
     *
     * @return размер объекта
     */
    int size();

    /**
     * Добавить все элементы из source в текущий контейнер, при совпадении ключей, суммировать значения
     *
     * @param source CountMap<T> объект для добавления
     */
    void addAll(CountMap<K> source);

    /**
     * Вернуть java.util.Map. Ключ - добавленный элемент, значение - количество его добавлений
     *
     * @return java.util.Map
     */
    Map<K, Integer> toMap();

    /**
     * Копирует элементы с текущей map на указанную map назначения.
     * <p>
     * По умолчанию этот метод работает в режиме перезаписи, заменяя любые существующие значения на карте назначения
     * значениями из текущей карты.
     *
     * @param destination map на которую копируются элементы текущей map
     * @throws NullPointerException если destination map is null
     */
    void toMap(Map<K, Integer> destination);

    /**
     * Копирует элементы из текущей map в указанную map назначения в режиме, определяемом параметром {@code combine}.
     * <p>
     * Если {@code combine} имеет значение {@code true}, метод суммирует значения повторяющихся ключей в map
     * назначения и текущей map. Если {@code combine} имеет значение {@code false}, метод перезаписывает значения в
     * map назначения значениями из текущей map.
     *
     * @param destination map на которую копируются элементы текущей map
     * @param combine     если {@code true}, суммирует значения для повторяющихся ключей; если {@code false},
     *                    перезаписывает значения на map назначения
     * @throws NullPointerException если the destination map is null
     */
    void toMap(Map<K, Integer> destination, boolean combine);

}
