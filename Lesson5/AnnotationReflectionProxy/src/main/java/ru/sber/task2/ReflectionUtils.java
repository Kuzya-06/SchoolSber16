package ru.sber.task2;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionUtils {
    public static void printAllMethods(Class<?> clazz) {
        if (clazz == null
//            || clazz.equals(Object.class) // Заканчиваем обработку, если дошли до базового класса Object
        ) {
            return;
        }

        System.out.println("Класс: " + clazz.getName());

        // Получаем все методы, включая приватные
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            String modifierString = Modifier.toString(method.getModifiers());
            System.out.println("  Метод: " + method.getName() + " | Модификаторы: " + modifierString);
        }

        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> interfazze : interfaces) {
            System.out.println("Интерфейс: "+interfazze);
            Method[] declaredMethodsInterface = interfazze.getDeclaredMethods();
            for (Method method : declaredMethodsInterface) {
                String modifierString = Modifier.toString(method.getModifiers());
                System.out.println("  Метод: " + method.getName() + " | Модификаторы: " + modifierString);
            }
        }

        printAllMethods(clazz.getSuperclass()); // Рекурсивно обрабатываем суперкласс
    }


}

