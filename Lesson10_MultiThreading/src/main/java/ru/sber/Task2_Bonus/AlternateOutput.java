package ru.sber.Task2_Bonus;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 1. Выведите на экран следующую строку через 2 потока без использования встроенной синхронизации:
 * 0 a 1 b 2 c 3 d 4 e 5 f 6 g 7 h 8 i 9 j,
 * где первый поток может выводить только цифры, а второй — буквы (буквы должны чередоваться друг с другом).
 */
public class AlternateOutput {
    public static void main(String[] args) {
        Object lock = new Object(); // Общий замок
        AtomicBoolean isNumberTurn = new AtomicBoolean(true); // Флаг, чей сейчас вывод

        Thread oneThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (!isNumberTurn.get()) { // Ждем своей очереди
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    System.out.print(i + " "); // Выводим число
                    isNumberTurn.set(false); // Передаем очередь букве
                    lock.notifyAll(); // Пробуждаем второй поток
                }
            }
        });

// 1 — «А», 2 — «В», 3 — «С», 4 — «D», 5 — «Е», 6 — «F», 7 — «G», 8 — «H», 9 — «I», 10 — «J», 11 — «K», 12 — «L», 13
// — «M», 14 — «N», 15 — «О», 16 — «P», 17 — «Q», 18 — «R», 19 — «S», 20 — «T», 21 — «U», 22 — «V», 23 — «W», 24 —
// «X», 25 — «Y», 26 — «Z»
        Thread twoThread = new Thread(() -> {
            for (char c = 97; c <= 106; c++) {
                synchronized (lock) {
                    while (isNumberTurn.get()) { // Ждем своей очереди
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    System.out.print(c + " "); // Выводим букву
                    isNumberTurn.set(true); // Передаем очередь числу
                    lock.notifyAll(); // Пробуждаем первый поток
                }
            }
        });


        oneThread.start();
        twoThread.start();

        try {
            oneThread.join();
            twoThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
