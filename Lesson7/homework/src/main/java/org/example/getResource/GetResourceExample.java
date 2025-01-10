package org.example.getResource;

/**
 * getResource(String name)
 * Возвращает объект URL для ресурса.
 * Пояснение:
 * Метод getResource сначала проверяет родительский загрузчик, а затем ищет ресурс.
 */
public class GetResourceExample {
    public static void main(String[] args) {
        ClassLoader classLoader = GetResourceExample.class.getClassLoader();
        var resourceUrl = classLoader.getResource("config.properties");

        System.out.println("Resource URL: " + resourceUrl);

    }
}
