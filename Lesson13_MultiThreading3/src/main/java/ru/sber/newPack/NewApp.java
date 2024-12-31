package ru.sber.newPack;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NewApp {

    private static ScheduledExecutorService sh = Executors.newScheduledThreadPool(2);

    public static void main(String[] args) {

        // Создать два потока ScheduledExecutorService
        // каждые 2 секунды запускается
        // один раз через 5 секунд

        sh.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("Я первая");
            }
        }, 3, 2, TimeUnit.SECONDS);


//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } finally {
////            sh.shutdown();
//        }

        sh.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("Я выполняюсь один раз через 5 секунд");
            }
        }, 5, TimeUnit.SECONDS);

        sh.schedule(new Runnable() {
            @Override
            public void run() {
                sh.shutdown();
                System.out.println("Executor завершён");
            }
        }, 6, TimeUnit.SECONDS);

    }

}
