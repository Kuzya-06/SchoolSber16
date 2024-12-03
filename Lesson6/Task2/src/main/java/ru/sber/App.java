package ru.sber;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        String[] arr = {"A", "B", "C"};
        ObjectArrayIterator<String> iterator = new ObjectArrayIterator<>(arr);
        for (String s : arr) {
            if (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        }
    }
}
