package org.example.load;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * loadClass(String name)
 * где name - логическое имя класса, связанное с файлом .class.
 * Загружает класс по имени с использованием цепочки делегирования.
 * Пояснение:
 * Метод loadClass сначала проверяет, загружен ли класс. Если нет, он делегирует запрос родительскому загрузчику.
 */
public class LoadClassExample {
    public static void main(String[] args) {
        //---------------------------------------------------------------------------------------
        try {
            // Загружаем класс ArrayList через ClassLoader
            ClassLoader classLoader = LoadClassExample.class.getClassLoader();
            Class<?> loadedClass = classLoader.loadClass("java.util.ArrayList");

            System.out.println("Class loaded: " + loadedClass.getName());

            // Создаем экземпляр ArrayList. Создается новый объект ArrayList.
            Object instance = loadedClass.getDeclaredConstructor().newInstance();

            // Находим метод add и вызываем его
            Method addMethod = loadedClass.getMethod("add", Object.class);
            addMethod.invoke(instance, "Hello, World!"); // Добавляем строку в ArrayList
            addMethod.invoke(instance, 55); // Добавляем строку в ArrayList
            addMethod.invoke(instance, "Hello, ClassLoader!"); // Добавляем строку в ArrayList

            // Находим метод get и вызываем его
            Method getMethod = loadedClass.getMethod("get", int.class);
            Object element = getMethod.invoke(instance, 2); // Получаем элемент по индексу 0

            // Вывод результата
            System.out.println("Element at index 0: " + element);
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        //---------------------------------------------------------------------------------------
        try {
            ClassLoader classLoader2 = LoadClassExample.class.getClassLoader();
            Class<?> loadedClass2 = classLoader2.loadClass("org.example.load.Person");

            Object instance = loadedClass2.getDeclaredConstructor().newInstance();
            Method addMethod = loadedClass2.getMethod("print", String.class, Integer.class);

            addMethod.invoke(instance, "Bob", 34);
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        //---------------------------------------------------------------------------------------
        try {
            ClassLoader classLoader3 = LoadClassExample.class.getClassLoader();
            Class<?> personClass = classLoader3.loadClass("org.example.load.Person");

            // Получаем конструктор с параметрами (String, int)
            Constructor<?> constructor = personClass.getConstructor(String.class, Integer.class);
            // Создаем экземпляр, передавая аргументы в конструктор
            Object personInstance = constructor.newInstance("Ежик", 23);
            // Вызываем метод printInfo на созданном экземпляре
            Method addMethod = personClass.getMethod("print");

            addMethod.invoke(personInstance);
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        //---------------------------------------------------------------------------------------
        try {
            ClassLoader classLoader2 = LoadClassExample.class.getClassLoader();
            /**
             * MyClass — это логическое имя класса, связанное с файлом .class.
             *
             */
            Class<?> loadedClass2 = classLoader2.loadClass(
                    "org.example.define.MyClass");

            Object instance = loadedClass2.getDeclaredConstructor().newInstance();
            Method addMethod = loadedClass2.getMethod("print");

            addMethod.invoke(instance);
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
