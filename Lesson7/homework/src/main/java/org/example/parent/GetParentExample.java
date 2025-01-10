package org.example.parent;

/**
 * getParent()
 * Возвращает родительский загрузчик.
 * Пояснение:
 * Полезно для анализа и понимания цепочки загрузчиков.
 */
public class GetParentExample {
    public static void main(String[] args) {
        CustomClassLoader classLoader = new CustomClassLoader();

        System.out.println("ClassLoader:                "+classLoader);

        ClassLoader parentLoader = classLoader.getParent();

        System.out.println("Parent ClassLoader:         " + parentLoader);
        System.out.println("Parent Parent ClassLoader: " + parentLoader.getParent());
        System.out.println("Parent Parent Parent ClassLoader: " + parentLoader.getParent().getParent());
    }
}

// Пользовательский загрузчик
class CustomClassLoader extends ClassLoader {

}