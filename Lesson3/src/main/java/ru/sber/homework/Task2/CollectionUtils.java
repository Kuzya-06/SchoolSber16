package ru.sber.homework.Task2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionUtils {

    /**
     * Копирует все элементы из source в destination
     * <p>
     * Этот метод позволяет копировать элементы из списка с элементами типа {@code T} или его подтипов
     * ({@code ? extends T}) в список, который может принимать элементы типа {@code T} или его супер типов
     * ({@code ? super T}).
     * </p>
     *
     * @param source      список, который нужно скопировать (не должно быть {@code null})
     * @param destination список, в который нужно скопировать (не должно быть {@code null})
     * @param <T>         тип копируемых элементов
     * @throws NullPointerException если, либо {@code source} или {@code destination} будет {@code null}
     */
    public static <T> void addAll(List<? extends T> source, List<? super T> destination) {
        destination.addAll(source);
    }

    /**
     * Создаёт и возвращает новый список, пустой {@link ArrayList}.
     *
     * @return новый список, пустой {@link ArrayList} типа {@code T}
     */
    public static <T> List<T> newArrayList() {
        return new ArrayList<>();
    }

    /**
     * Возвращает индекс первого вхождения объекта o в списке source или -1, если элемент не найден
     *
     * @param source список
     * @param o      объект
     * @return индекс первого вхождения объекта
     */
    public static <T> int indexOf(List<? extends T> source, T o) {
        return source.indexOf(o);
    }

    /**
     * Возвращает новый список, содержащий первые size элементов исходного списка
     *
     * @param source список
     * @param size   размер выводимого списка
     * @param <T>    тип элементов в списке
     * @return новый список, содержащий первые size элементов
     */
    public static <T> List<T> limit(List<? extends T> source, int size) {
        return source.stream().limit(size).collect(Collectors.toList());
    }

    /**
     * Добавляет элемент в список
     *
     * @param source список, в который добавляется элемент
     * @param o      элемент, который нужно добавить
     */
    public static <T> void add(List<? super T> source, T o) {
        source.add(o);
    }

    /**
     * Удаляет все элементы c2 из списка removeFrom
     *
     * @param removeFrom список, в котором удаляются элементы
     * @param c2         список, содержащий элементы, которые нужно удалить из списка removeFrom
     * @param <T>        тип элементов в списке
     */
    public static <T> void removeAll(List<? super T> removeFrom, List<? extends T> c2) {
        removeFrom.removeAll(c2);
    }

    /**
     * Возвращает true, если первый список содержит все элементы второго списка
     *
     * @param c1  первый список
     * @param c2  второй список
     * @param <T> тип элементов в списке
     * @return true, если этот список содержит указанный элемент
     */
    public static <T> boolean containsAll(List<? super T> c1, List<? extends T> c2) {
        return new HashSet<>(c1).containsAll(c2);
    }

    /**
     * Возвращает true, если первый лист содержит хотя-бы 1 второго
     *
     * @param c1 первый список
     * @param c2 второй список
     * @return true, если первый лист содержит хотя-бы 1 второго
     */
    public static <T> boolean containsAny(List<? super T> c1, List<? extends T> c2) {
        for (T item : c2) {
            if (c1.contains(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Возвращает список, содержащий элементы из list в диапазоне от {@code min} до {@code max}. Элементы сравнивать
     * через Comparable.
     * <p><b>Пример:</b> </p>
     * <pre>
     *     range(Arrays.asList(8,1,3,5,6, 4), 3, 6)
     *     вернет {3,4,5,6}
     * </pre>
     *
     * @param list входной список для фильтрации и сортировки
     * @param min  минимальное значение диапазона (включительно)
     * @param max  максимальное значение диапазона (включительно)
     * @param <T>  тип элементов в списке
     * @return список, содержащий элементы из list в диапазоне от min до max
     * @throws NullPointerException если {@code list} или {@code comparator} будут {@code null}
     */
    public static <T extends Comparable<? super T>> List<T> range(List<? extends T> list, T min, T max) {
        List<T> result = new ArrayList<>();
        for (T item : list) {
            if (item.compareTo(min) >= 0 && item.compareTo(max) <= 0) {
                result.add(item);
            }
        }
        Collections.sort(result);
        return result;
    }

    /**
     * Возвращает список, содержащий элементы из list в диапазоне от {@code min} до {@code max}. Элементы сравнивать
     * через Comparator.
     * <p><b>Пример:</b> </p>
     * <pre>
     *     range(Arrays.asList(8,1,3,5,6, 4), 3, 6, Comparator.naturalOrder())
     *     вернет {3,4,5,6}
     * </pre>
     *
     * @param list       входной список для фильтрации и сортировки
     * @param min        минимальное значение диапазона (включительно)
     * @param max        максимальное значение диапазона (включительно)
     * @param comparator параметр {@link Comparator}, используемый для сравнения элементов (не должен быть {@code null})
     * @param <T>        тип элементов в списке
     * @return список, содержащий элементы из list в диапазоне от min до max
     * @throws NullPointerException если {@code list} или {@code comparator} будут {@code null}
     */
    public static <T> List<T> range(List<? extends T> list, T min, T max, Comparator<? super T> comparator) {
        List<T> result = new ArrayList<>();
        for (T item : list) {
            if (comparator.compare(item, min) >= 0 && comparator.compare(item, max) <= 0) {
                result.add(item);
            }
        }
        result.sort(comparator);
        return result;
    }

}
