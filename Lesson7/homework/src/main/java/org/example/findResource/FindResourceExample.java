package org.example.findResource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

/**
 * findResource(String name)
 * Находит ресурс с заданным именем.
 * Пояснение:
 * Находит ресурс (например, .properties файл) в пути загрузчика.
 */

public class FindResourceExample extends ClassLoader {
    private static final Log log = LogFactory.getLog(FindResourceExample.class);

    @Override
    protected URL findResource(String name) {
        try {
            // Путь к каталогу, где искать ресурсы
            ClassLoader classLoader = FindResourceExample.class.getClassLoader();

            Path filePath = Path.of(Objects.requireNonNull(classLoader.getResource(name)).toURI());
            log.info(filePath); // "D:\\SchoolJavaSber\\project\\SchoolSber16\\Lesson7\\homework\\target\\classes\\config.properties"
            File resourceFile = new File(filePath.toUri());

            if (resourceFile.exists()) {
                try {
                    // Преобразуем файл в URL
                    return resourceFile.toURI().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return null; // Если ресурс не найден
    }

    public static void main(String[] args) {
        // Создаем экземпляр кастомного ClassLoader
        FindResourceExample customClassLoader = new FindResourceExample();

        // Ищем ресурс config.properties
        URL resourceUrl = customClassLoader.findResource("config.properties");

        System.out.println("Resource URL: " + resourceUrl);
    }
}