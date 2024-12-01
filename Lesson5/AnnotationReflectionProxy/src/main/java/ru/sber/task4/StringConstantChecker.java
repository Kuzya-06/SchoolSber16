package ru.sber.task4;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class StringConstantChecker {
    // Цвета текста
    private static final String RESET = "\u001B[0m";   // Сброс цвета
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";

    public static void checkStringConstants(Class<?> clazz) {
        System.out.println("Проверить что все String константы имеют значение = их имени | в классе: " + clazz.getName());

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            System.out.println("Проверяем: " + field);
            // Проверяем, что поле является public static final
            if (Modifier.isPublic(field.getModifiers()) &&
                    Modifier.isStatic(field.getModifiers()) &&
                    Modifier.isFinal(field.getModifiers()) &&
                    field.getType() == String.class) {

                String value = null;
                try {
                    value = (String) field.get(null); // Получаем значение константы
                } catch (IllegalAccessException e) {
                    System.err.println("Нет доступ к полю: " + field.getName());
                }
                if (field.getName().equals(value)) {
                        System.out.println(GREEN + "Поле " + field.getName() + " корректно" + RESET);
                    } else {
                        System.out.println(RED + "Поле " + field.getName() + " имеет значение \"" + value + "\", " +
                                "но должно быть \"" + field.getName() + "\"" + RESET);
                    }
                }
            }
        }



    public static void main(String[] args) {
        checkStringConstants(DaysOfWeek.class);
    }
}

