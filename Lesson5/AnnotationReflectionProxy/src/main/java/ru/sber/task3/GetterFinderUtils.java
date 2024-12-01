package ru.sber.task3;

import java.lang.reflect.Method;

public class GetterFinderUtils {

    public static void printAllGetters(Class<?> clazz) {

        if (clazz == null
//            || clazz.equals(Object.class) // Заканчиваем обработку, если дошли до базового класса Object
        ) {
            return;
        }
        System.out.println("Геттеры класса: " + clazz.getName());

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (isGetter(method)) {
                System.out.println("  Геттер: " + method.getName() + " | Возвращаемый тип: " + method.getReturnType().getName());
            }
        }
        printAllGetters(clazz.getSuperclass());
    }

    private static boolean isGetter(Method method) {
        return method.getName().startsWith("get")
                && method.getName().length() > 3
                && method.getReturnType() != void.class
                && method.getParameterCount() == 0;
    }
}
