package ru.sbt;

public class MyPlugin implements Plugin {
    @Override
    public void doUsefull() {
        String[] spinner = {"|", "/", "-", "\\"}; // Символы для анимации вращения
        int delay = 200; // Задержка в миллисекундах между кадрами

        System.out.println("Запуск приложения MyPlugin ...   ");

        // Цикл анимации
        for (int i = 0; i < 50; i++) { // Цикл из 50 итераций
            System.out.print("\r" + spinner[i % spinner.length]); // \r возвращает курсор в начало строки
             // Задержка
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                break;
            }
        }

        System.out.print("\r    Готово! Загружен MyPlugin\n"); // Финальное сообщение
    }
}
