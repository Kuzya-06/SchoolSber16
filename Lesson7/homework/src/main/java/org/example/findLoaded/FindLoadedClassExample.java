package org.example.findLoaded;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.define.DefineClassExample;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * findLoadedClass(String name)
 * Проверяет, загружен ли уже класс.
 * Пояснение:
 * Предотвращает повторную загрузку класса.
 */
public class FindLoadedClassExample {
    private static final Log log = LogFactory.getLog(FindLoadedClassExample.class);

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, URISyntaxException, IOException {
        ClassLoader classLoader = FindLoadedClassExample.class.getClassLoader();
        String str = "org/example/define/MyClass.class";
        Path filePath =
                Path.of(Objects.requireNonNull(classLoader.getResource(str)).toURI());
        log.info(filePath);

        // Загрузите байтовый код класса
        byte[] classBytes = Files.readAllBytes(filePath);

        // Создаём кастомный ClassLoader
        CustomClassLoader customClassLoader = new CustomClassLoader();
        String substring = str.replace("/", ".").substring(0, str.lastIndexOf("."));
        log.info(substring); // org.example.define.MyClass

        // Загружаем класс стандартным способом
        Class<?> clazz = customClassLoader.defineClassFromBytes(substring, classBytes);
        Object o = clazz.getDeclaredConstructor().newInstance();
        clazz.getMethod("sayHello").invoke(o);

        // Проверяем, был ли класс загружен
        Class<?> loadedClass = customClassLoader.findLoadedClassN(substring);

        log.info("      Класс уже загружен: " + (loadedClass != null) + "\n");
    }
}

class CustomClassLoader extends ClassLoader {
    public Class<?> defineClassFromBytes(String name, byte[] bytes) {
        System.out.println("    Вызов метода defineClass() " + name + " " + bytes.length);
        return defineClass(name, bytes, 0, bytes.length);
    }

    public Class<?> findLoadedClassN(String name) {
        System.out.println("    Вызов метода findLoadedClassN() " + name);
        return findLoadedClass(name);
    }
}