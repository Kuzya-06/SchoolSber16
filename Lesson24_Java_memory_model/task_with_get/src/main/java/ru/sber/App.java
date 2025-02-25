package ru.sber;


import java.util.Random;

public class App {

    public static void main(String[] args) {

        Task<Integer> task = new Task<>(() -> {
            Thread.sleep(2000); // Имитация долгого вычисления
            Random random = new Random();
            int randomValue = random.nextInt(10);
            if(randomValue >5){
                throw new Exception("Случилось что-то страшное. Выпало - "+randomValue);
            }
            return randomValue;
        });

        Runnable runnable = () -> {
            try {
                System.out.println("Ответ: " + task.get());
            } catch (Task.TaskExecutionException e) {
                System.out.println("Исключение: " + e.getMessage());
                Throwable cause = e.getCause();
                System.out.println(cause.getMessage());
            }
        };

        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        Thread thread3 = new Thread(runnable);

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
