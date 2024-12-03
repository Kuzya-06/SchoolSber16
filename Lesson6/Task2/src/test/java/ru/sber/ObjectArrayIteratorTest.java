package ru.sber;


import jdk.jfr.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ObjectArrayIteratorTest {
    private String[] data;

    @BeforeEach
    public void setUp() {
        data = new String[]{"A", "B", "C"};
    }

    @Test
    @Name(value = "Перебор элементов")
    void testShouldIterateOverElements() {

        ObjectArrayIterator<String> iterator = new ObjectArrayIterator<>(data);

        assertTrue(iterator.hasNext());
        assertEquals("A", iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals("B", iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals("C", iterator.next());

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Name(value = "Обработка пустого массива")
    void testShouldHandleEmptyArray() {
        Integer[] data = {};
        ObjectArrayIterator<Integer> iterator = new ObjectArrayIterator<>(data);

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Name(value = "Исключение для нулевого массива")
    void testShouldThrowExceptionForNullArray() {
        assertThrows(IllegalArgumentException.class,
                () -> new ObjectArrayIterator<>(null));
    }
}
