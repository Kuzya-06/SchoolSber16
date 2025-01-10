package org.example.findClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * findClass(String name)
 * Используется в пользовательских загрузчиках для самостоятельной загрузки классов.
 * Пояснение:
 * Этот метод переопределяется, когда нужно указать нестандартное поведение для загрузки байтового кода.
 */
public class CustomClassLoader extends ClassLoader {
    private int count = 0;
    private final File dir;

    public CustomClassLoader(File dir) {
        this.dir = dir;
    }

    /**
     * Логика работы:
     * При вызове loadClass сперва проверяется, был ли класс уже загружен.
     * Если нет, вызывается findClass, который загружает байты класса.
     * defineClass преобразует байты в объект Class.
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        System.out.println("loadClass-"+ ++count+" called for: " + name);

        // Проверяем, был ли класс уже загружен
        Class<?> c = findLoadedClass(name);

        if (c == null) {
            try {
                System.out.println("    Не делегируем родителю, а вызываем findClass");
                // Не делегируем родителю, а вызываем findClass
                c = findClass(name);
            } catch (ClassNotFoundException e) {
                System.out.println("    Если findClass не справился, отдаем родителю");
                // Если findClass не справился, отдаем родителю
                c = super.loadClass(name);
            }
        }
        System.out.println("C = " + c);
        return c;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            System.out.println("        findClass called for: " + name);
            Path filePath = dir.toPath().resolve(name);

            byte[] bytes = Files.readAllBytes(filePath);

            // defineClass преобразует байтовый код в объект Class.
            String name1= "org.example.findClass."+name.substring(0, name.indexOf("."));
            return defineClass(name1, bytes, 0, bytes.length);
        } catch (IOException exception) {
            throw new ClassNotFoundException(String.format("Ошибка загрузки класса: %s", name), exception);
        }
    }

}
