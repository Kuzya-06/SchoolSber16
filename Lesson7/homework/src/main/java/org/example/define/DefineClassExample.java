package org.example.define;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * defineClass(String name, byte[] b, int off, int len)
 * Создает объект Class из массива байтов.
 * Пояснение:
 * Этот метод используется, если байты класса были загружены из нестандартного источника.
 */
public class DefineClassExample {
    private static final Log log = LogFactory.getLog(DefineClassExample.class);

    public static void main(String[] args) throws Exception {
        ClassLoader classLoader = DefineClassExample.class.getClassLoader();
        String str = "org/example/define/MyClass.class";
        Path filePath =
                Path.of(Objects.requireNonNull(classLoader.getResource(str)).toURI());
        log.info(filePath);

        // Загрузите байтовый код класса
        byte[] classBytes = Files.readAllBytes(filePath);


        // Создайте пользовательский загрузчик
        CustomClassLoader customClassLoader = new CustomClassLoader();

        // Определите класс из байтов
        Class<?> clazz = customClassLoader.defineClassFromBytes("org.example.define.MyClass", classBytes);

        // Проверьте имя класса (если оно указано в байтовом коде)
        System.out.println("Class defined: " + clazz.getName());

        // Создайте экземпляр класса
        Object instance = clazz.getDeclaredConstructor().newInstance();
        System.out.println("Instance created: " + instance);

        clazz.getMethod("sayHello").invoke(instance);

    }
}

// Пользовательский загрузчик
class CustomClassLoader extends ClassLoader {
    public Class<?> defineClassFromBytes(String name, byte[] bytes) {
        System.out.println("Вызов метода defineClass()");
        return defineClass(name, bytes, 0, bytes.length);
    }
}