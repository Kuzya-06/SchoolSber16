package org.example.findClass;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Метод findClass вызывается ClassLoader-ом только если:
 * 1. Родительский загрузчик не смог найти класс.
 * 2. Вы явно вызываете findClass вместо loadClass.
 * Когда вы используете loadClass, он сперва вызывает родительский загрузчик. Если родительский загрузчик успешно
 * загружает класс, то ваш метод findClass не вызывается.
 *
 * Как заставить findClass работать?
 * 1. Создайте экземпляр вашего CustomClassLoader без родительского загрузчика.
 * - По умолчанию ClassLoader делегирует загрузку родителю. Чтобы этого избежать, используйте конструктор без
 * делегирования.
 * 2. Переопределите метод loadClass в вашем CustomClassLoader.
 * - Вызовите findClass напрямую.
 */
public class MainFindClass {
    public static void main(String[] args) {
        try {
            String dir = "D:\\SchoolJavaSber\\project\\SchoolSber16\\Lesson7\\homework\\target\\classes\\org\\example\\findClass\\";
            // Создаем экземпляр нашего CustomClassLoader
            CustomClassLoader customLoader = new CustomClassLoader(new File(dir));
//            ClassLoader customLoader = MainFindClass.class.getClassLoader();

            // Загружаем класс
            Class<?> loadedClass = customLoader.loadClass("MyCustomClass.class");
//            Class<?> loadedClass = customLoader.loadClass("org.example.findClass.MyCustomClass");

            // Создаем экземпляр загруженного класса
            Object instance = loadedClass.getDeclaredConstructor().newInstance();

            // Вызов метода класса
            Method method = loadedClass.getMethod("sayHello");
            method.invoke(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
